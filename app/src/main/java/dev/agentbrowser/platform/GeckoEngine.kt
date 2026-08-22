package dev.agentbrowser.platform

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.GeckoRuntimeSettings

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

    private val progressDelegate = object : GeckoSession.ProgressDelegate {
        override fun onProgressChange(session: GeckoSession?, progress: Int) {
            _loadingProgress.value = progress
            _isLoading.value = progress < 100
        }
    }

    private val navigationDelegate = object : GeckoSession.NavigationDelegate {
        override fun onCanGoBack(session: GeckoSession?, canGoBack: Boolean) {
            _canGoBack.value = canGoBack
        }

        override fun onCanGoForward(session: GeckoSession?, canGoForward: Boolean) {
            _canGoForward.value = canGoForward
        }

        override fun onLocationChange(session: GeckoSession?, url: String?) {
            _currentUrl.value = url ?: ""
        }
    }

    private val contentDelegate = object : GeckoSession.ContentDelegate {
        override fun onTitleChange(session: GeckoSession?, title: String?) {
            _currentTitle.value = title ?: ""
        }

        override fun onFaviconChange(session: GeckoSession?, favicon: android.graphics.Bitmap?) {
        }

        override fun onPageStart(session: GeckoSession?, url: String?) {
            _isLoading.value = true
            _loadingProgress.value = 0
            _currentUrl.value = url ?: ""
        }

        override fun onPageStop(session: GeckoSession?, success: Boolean) {
            _isLoading.value = false
            _loadingProgress.value = if (success) 100 else 0
        }
    }

    init {
        runtime = GeckoRuntime.create(context)
    }

    fun createSession(tabId: String): GeckoSession {
        val session = GeckoSession()
        session.setProgressDelegate(progressDelegate)
        session.setNavigationDelegate(navigationDelegate)
        session.setContentDelegate(contentDelegate)
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
        geckoView.setSession(session)
        if (session != null) {
            _canGoBack.value = session.canGoBack()
            _canGoForward.value = session.canGoForward()
            _currentUrl.value = session.currentUrl ?: ""
            _currentTitle.value = session.currentTitle ?: ""
        }
    }

    fun getActiveSession(): GeckoSession? = geckoView.getSession()

    fun getGeckoView(): GeckoView = geckoView

    fun loadUrl(url: String, tabId: String) {
        val session = sessions[tabId] ?: createSession(tabId)
        session.loadUri(url)
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
