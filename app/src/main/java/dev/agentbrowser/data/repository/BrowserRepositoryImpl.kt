package dev.agentbrowser.data.repository

import dev.agentbrowser.domain.model.BrowserState
import dev.agentbrowser.domain.model.HistoryEntry
import dev.agentbrowser.domain.model.SearchProvider
import dev.agentbrowser.domain.model.Tab
import dev.agentbrowser.domain.model.GoogleSearchProvider
import dev.agentbrowser.domain.repository.BrowserRepository
import dev.agentbrowser.domain.repository.HistoryRepository
import dev.agentbrowser.domain.repository.TabManager
import dev.agentbrowser.platform.WebViewEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class BrowserRepositoryImpl(
    private val engine: WebViewEngine,
    private val tabManager: TabManager,
    private val historyRepository: HistoryRepository,
    private val searchProvider: SearchProvider = GoogleSearchProvider
) : BrowserRepository {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _browserState = MutableStateFlow(BrowserState())
    override val browserState: StateFlow<BrowserState> = _browserState.asStateFlow()

    init {
        scope.launch {
            tabManager.activeTabId.collect { activeTabId ->
                val activeTab = activeTabId?.let { tabManager.getTab(it) }
                _browserState.update { state ->
                    state.copy(
                        activeTabId = activeTabId,
                        tabs = tabManager.tabs.value,
                        currentUrl = activeTab?.url ?: engine.currentUrl.value,
                        currentTitle = activeTab?.title ?: engine.currentTitle.value,
                        isLoading = engine.isLoading.value,
                        loadingProgress = engine.loadingProgress.value,
                        canGoBack = engine.canGoBack.value,
                        canGoForward = engine.canGoForward.value,
                        error = engine.error.value
                    )
                }
            }
        }
        scope.launch {
            engine.loadingProgress.collect { _browserState.update { it.copy(loadingProgress = it.loadingProgress) } }
        }
        scope.launch {
            engine.isLoading.collect { _browserState.update { it.copy(isLoading = it.isLoading) } }
        }
        scope.launch {
            engine.error.collect { _browserState.update { it.copy(error = it.error) } }
        }
    }

    override suspend fun loadUrl(url: String, tabId: String?) {
        val targetTabId = tabId ?: tabManager.activeTabId.value ?: run {
            val newTab = tabManager.createTab()
            newTab.id
        }
        val resolvedUrl = resolveUrl(url)
        engine.loadUrl(resolvedUrl, targetTabId)
        if (tabId == null) {
            tabManager.switchTab(targetTabId)
        }
        val tab = tabManager.getTab(targetTabId)
        if (tab != null) {
            historyRepository.addEntry(
                HistoryEntry(
                    url = resolvedUrl,
                    title = resolvedUrl
                )
            )
            tabManager.updateTab(targetTabId) { it.copy(url = resolvedUrl) }
        }
    }

    override suspend fun goBack(tabId: String?) {
        val targetTabId = tabId ?: tabManager.activeTabId.value ?: return
        engine.goBack(targetTabId)
    }

    override suspend fun goForward(tabId: String?) {
        val targetTabId = tabId ?: tabManager.activeTabId.value ?: return
        engine.goForward(targetTabId)
    }

    override suspend fun refresh(tabId: String?) {
        val targetTabId = tabId ?: tabManager.activeTabId.value ?: return
        engine.reload(targetTabId)
    }

    override suspend fun stopLoading(tabId: String?) {
        val targetTabId = tabId ?: tabManager.activeTabId.value ?: return
        engine.stopLoading(targetTabId)
    }

    override suspend fun clearError() {
        engine.clearError()
        _browserState.update { it.copy(error = null) }
    }

    override fun getActiveTab(): StateFlow<Tab?> {
        return tabManager.activeTabId.map { id ->
            id?.let { tabManager.getTab(it) }
        }.stateIn(scope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), null)
    }

    private fun resolveUrl(input: String): String {
        return when {
            input.startsWith("http://") || input.startsWith("https://") -> input
            input.contains(".") && !input.contains(" ") -> "https://$input"
            else -> searchProvider.buildSearchUrl(input)
        }
    }
}
