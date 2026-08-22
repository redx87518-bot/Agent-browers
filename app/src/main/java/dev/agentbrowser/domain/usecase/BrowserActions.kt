package dev.agentbrowser.domain.usecase

sealed interface BrowserAction {
    data class OpenUrl(val url: String, val inNewTab: Boolean = false) : BrowserAction
    data class Search(val query: String) : BrowserAction
    data object GoBack : BrowserAction
    data class GoForward(val tabId: String? = null) : BrowserAction
    data class Refresh(val tabId: String? = null) : BrowserAction
    data object StopLoading : BrowserAction
    data class CloseTab(val tabId: String) : BrowserAction
    data class SwitchTab(val tabId: String) : BrowserAction
    data object CloseAllTabs : BrowserAction
    data object ClearError : BrowserAction
}
