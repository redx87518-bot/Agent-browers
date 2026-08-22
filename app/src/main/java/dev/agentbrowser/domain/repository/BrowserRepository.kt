package dev.agentbrowser.domain.repository

import dev.agentbrowser.domain.model.BrowserState
import dev.agentbrowser.domain.model.Tab
import kotlinx.coroutines.flow.StateFlow

interface BrowserRepository {
    val browserState: StateFlow<BrowserState>
    suspend fun loadUrl(url: String, tabId: String? = null)
    suspend fun goBack(tabId: String? = null)
    suspend fun goForward(tabId: String? = null)
    suspend fun refresh(tabId: String? = null)
    suspend fun stopLoading(tabId: String? = null)
    suspend fun clearError()
    fun getActiveTab(): StateFlow<Tab?>
}
