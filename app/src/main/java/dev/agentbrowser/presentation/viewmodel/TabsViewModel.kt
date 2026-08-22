package dev.agentbrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.agentbrowser.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class TabsViewModel(
    private val tabManager: dev.agentbrowser.domain.repository.TabManager,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val tabs = tabManager.tabs
    val activeTabId = tabManager.activeTabId

    val history = historyRepository.getRecentHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun newTab() = viewModelScope.launch {
        val tab = tabManager.createTab()
        tabManager.switchTab(tab.id)
    }

    fun closeTab(tabId: String) = viewModelScope.launch {
        tabManager.closeTab(tabId)
    }

    fun switchTab(tabId: String) = viewModelScope.launch {
        tabManager.switchTab(tabId)
    }

    fun clearHistory() = viewModelScope.launch {
        historyRepository.clearHistory()
    }
}
