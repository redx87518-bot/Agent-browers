package dev.agentbrowser.platform

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

class GeckoEngine(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val runtime: GeckoRuntime
    private val sessions = mutableMapOf<String, GeckoSession>()
    private val geckoView = GeckoView(context)

    private val _loadingProgress = MutableStateFlow(0)
    val loadingProgress: StateFlow<Int> = _loadingProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentUrl = MutableStateFlow("")
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()

    private val _currentTitle = MutableStateFlow("")
    val currentTitle: StateFlow<String> = _currentTitle.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val _canGoForward = MutableStateFlow(false)
    val canGoForward: StateFlow<Boolean> = _canGoForward.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        runtime = GeckoRuntime.create(context)
    }

    fun createSession(tabId: String): GeckoSession {
        val session = GeckoSession()
        session.open(runtime)
        sessions[tabId] = session
        return session
    }

    fun getSession(tabId: String): GeckoSession? = sessions[tabId]

    fun closeSession(tabId: String) {
        sessions.remove(tabId)?.close()
    }

    fun setActiveSession(tabId: String?) {
        val session = sessions[tabId]
        if (session != null) {
            geckoView.setSession(session)
        }
    }

    fun getActiveSession(): GeckoSession? = geckoView.getSession()

    fun getGeckoView(): GeckoView = geckoView

    fun loadUrl(url: String, tabId: String) {
        val session = sessions[tabId] ?: createSession(tabId)
        session.loadUri(url)
        _currentUrl.value = url
        _isLoading.value = true
        _loadingProgress.value = 0
    }

    fun goBack(tabId: String) {
        sessions[tabId]?.goBack()
    }

    fun goForward(tabId: String) {
        sessions[tabId]?.goForward()
    }

    fun reload(tabId: String) {
        sessions[tabId]?.reload()
    }

    fun stopLoading(tabId: String) {
        sessions[tabId]?.stop()
    }

    fun clearError() {
        _error.value = null
    }
}
