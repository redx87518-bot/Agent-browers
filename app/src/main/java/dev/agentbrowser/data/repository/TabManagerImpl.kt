package dev.agentbrowser.data.repository

import dev.agentbrowser.data.local.dao.TabDao
import dev.agentbrowser.data.local.mapper.TabMapper
import dev.agentbrowser.domain.model.Tab
import dev.agentbrowser.domain.repository.TabManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TabManagerImpl(
    private val tabDao: TabDao,
    private val engine: GeckoEngine
) : TabManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _tabs = MutableStateFlow<List<Tab>>(emptyList())
    override val tabs: StateFlow<List<Tab>> = _tabs.asStateFlow()

    private val _activeTabId = MutableStateFlow<String?>(null)
    override val activeTabId: StateFlow<String?> = _activeTabId.asStateFlow()

    init {
        scope.launch {
            tabDao.getAllTabs().collectLatest { entities ->
                val tabs = entities.map { TabMapper.toDomain(it) }
                _tabs.value = tabs
                if (_activeTabId.value == null && tabs.isNotEmpty()) {
                    _activeTabId.value = tabs.first().id
                }
            }
        }
    }

    override suspend fun createTab(url: String = ""): Tab {
        val tab = Tab(url = url)
        tabDao.insertTab(TabMapper.toEntity(tab))
        engine.createSession(tab.id)
        return tab
    }

    override suspend fun closeTab(tabId: String) {
        val tab = getTab(tabId)
        if (tab != null) {
            engine.closeSession(tabId)
            tabDao.deleteTabById(tabId)
            if (_activeTabId.value == tabId) {
                val remaining = _tabs.value.filter { it.id != tabId }
                _activeTabId.value = remaining.firstOrNull()?.id
                if (remaining.isNotEmpty()) {
                    engine.setActiveSession(remaining.first().id)
                }
            }
        }
    }

    override suspend fun switchTab(tabId: String) {
        val tab = getTab(tabId)
        if (tab != null) {
            engine.setActiveSession(tabId)
            _activeTabId.value = tabId
            val updatedTab = tab.copy(lastActiveAt = System.currentTimeMillis())
            tabDao.insertTab(TabMapper.toEntity(updatedTab))
        }
    }

    override suspend fun closeAllTabs() {
        _tabs.value.forEach { engine.closeSession(it.id) }
        tabDao.deleteAllTabs()
        _tabs.value = emptyList()
        _activeTabId.value = null
    }

    override suspend fun updateTab(tabId: String, transform: (Tab) -> Tab) {
        val currentTab = getTab(tabId) ?: return
        val updatedTab = transform(currentTab)
        tabDao.insertTab(TabMapper.toEntity(updatedTab))
    }

    override fun getTab(tabId: String): Tab? {
        return _tabs.value.find { it.id == tabId }
    }

    override fun getActiveTab(): Tab? {
        return _activeTabId.value?.let { getTab(it) }
    }
}
