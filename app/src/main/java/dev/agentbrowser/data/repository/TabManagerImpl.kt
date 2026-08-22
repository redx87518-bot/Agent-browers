package dev.agentbrowser.data.repository

import dev.agentbrowser.domain.model.Tab
import dev.agentbrowser.domain.repository.TabManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TabManagerImpl(
    private val engine: GeckoEngine
) : TabManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _tabs = MutableStateFlow<List<Tab>>(emptyList())
    override val tabs: StateFlow<List<Tab>> = _tabs.asStateFlow()

    private val _activeTabId = MutableStateFlow<String?>(null)
    override val activeTabId: StateFlow<String?> = _activeTabId.asStateFlow()

    override suspend fun createTab(url: String): Tab {
        val tab = Tab(url = url)
        _tabs.update { it + tab }
        engine.createSession(tab.id)
        if (_activeTabId.value == null) {
            _activeTabId.value = tab.id
        }
        return tab
    }

    override suspend fun closeTab(tabId: String) {
        val tab = _tabs.value.find { it.id == tabId } ?: return
        engine.closeSession(tabId)
        _tabs.update { it.filter { t -> t.id != tabId } }
        if (_activeTabId.value == tabId) {
            _activeTabId.value = _tabs.value.firstOrNull()?.id
            if (_activeTabId.value != null) {
                engine.setActiveSession(_activeTabId.value)
            }
        }
    }

    override suspend fun switchTab(tabId: String) {
        val tab = _tabs.value.find { it.id == tabId } ?: return
        engine.setActiveSession(tabId)
        _activeTabId.value = tabId
        _tabs.update { list ->
            list.map { if (it.id == tabId) it.copy(lastActiveAt = System.currentTimeMillis()) else it }
        }
    }

    override suspend fun closeAllTabs() {
        _tabs.value.forEach { engine.closeSession(it.id) }
        _tabs.value = emptyList()
        _activeTabId.value = null
    }

    override suspend fun updateTab(tabId: String, transform: (Tab) -> Tab) {
        _tabs.update { list ->
            list.map { if (it.id == tabId) transform(it) else it }
        }
    }

    override fun getTab(tabId: String): Tab? {
        return _tabs.value.find { it.id == tabId }
    }

    override fun getActiveTab(): Tab? {
        return _activeTabId.value?.let { getTab(it) }
    }
}
