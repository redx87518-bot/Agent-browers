package dev.agentbrowser.domain.model

data class BrowserState(
    val activeTabId: String? = null,
    val tabs: List<Tab> = emptyList(),
    val currentUrl: String = "",
    val currentTitle: String = "",
    val isLoading: Boolean = false,
    val loadingProgress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val error: String? = null
)
