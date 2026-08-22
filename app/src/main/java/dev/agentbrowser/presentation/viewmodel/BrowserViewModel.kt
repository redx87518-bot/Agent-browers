package dev.agentbrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.agentbrowser.domain.repository.BrowserRepository
import dev.agentbrowser.domain.repository.TabManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class BrowserViewModel(
    private val browserRepository: BrowserRepository,
    private val tabManager: TabManager
) : ViewModel() {

    val browserState = browserRepository.browserState
    val tabs = tabManager.tabs
    val activeTabId = tabManager.activeTabId

    init {
        viewModelScope.launch {
            tabManager.activeTabId.collect {
                updateActiveTab()
            }
        }
    }

    private fun updateActiveTab() {
        val activeTab = tabManager.activeTabId.value?.let { tabManager.getTab(it) }
    }

    fun loadUrl(url: String) = viewModelScope.launch {
        browserRepository.loadUrl(url)
    }

    fun goBack() = viewModelScope.launch {
        browserRepository.goBack()
    }

    fun goForward() = viewModelScope.launch {
        browserRepository.goForward()
    }

    fun refresh() = viewModelScope.launch {
        browserRepository.refresh()
    }

    fun stopLoading() = viewModelScope.launch {
        browserRepository.stopLoading()
    }

    fun clearError() = viewModelScope.launch {
        browserRepository.clearError()
    }

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
}
