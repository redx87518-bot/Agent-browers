package dev.agentbrowser.domain.repository

import dev.agentbrowser.domain.model.Tab
import kotlinx.coroutines.flow.StateFlow

interface TabManager {
    val tabs: StateFlow<List<Tab>>
    val activeTabId: StateFlow<String?>
    suspend fun createTab(url: String = ""): Tab
    suspend fun closeTab(tabId: String)
    suspend fun switchTab(tabId: String)
    suspend fun closeAllTabs()
    fun getTab(tabId: String): Tab?
    fun getActiveTab(): Tab?
}
