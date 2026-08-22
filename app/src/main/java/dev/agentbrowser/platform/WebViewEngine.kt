package dev.agentbrowser.platform

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WebViewEngine(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val webViews = mutableMapOf<String, WebView>()
    private var activeTabId: String? = null

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

    fun createWebView(tabId: String): WebView {
        val webView = WebView(context)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                _error.value = null
                _isLoading.value = true
                _loadingProgress.value = 0
                _currentUrl.value = url ?: ""
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                _isLoading.value = false
                _loadingProgress.value = 100
                _currentUrl.value = url ?: ""
                _canGoBack.value = view?.canGoBack() ?: false
                _canGoForward.value = view?.canGoForward() ?: false
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                if (request?.isForMainFrame == true) {
                    _isLoading.value = false
                    _error.value = error?.description?.toString() ?: "Unknown error"
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                _loadingProgress.value = newProgress
                _isLoading.value = newProgress < 100
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                _currentTitle.value = title ?: ""
            }
        }

        webViews[tabId] = webView
        return webView
    }

    fun getWebView(tabId: String): WebView? = webViews[tabId]

    fun closeWebView(tabId: String) {
        webViews.remove(tabId)?.apply {
            stopLoading()
            destroy()
        }
        if (activeTabId == tabId) {
            activeTabId = null
        }
    }

    fun setActiveWebView(tabId: String?) {
        activeTabId = tabId
        val webView = webViews[tabId]
        if (webView != null) {
            _canGoBack.value = webView.canGoBack()
            _canGoForward.value = webView.canGoForward()
            _currentUrl.value = webView.url ?: ""
            _currentTitle.value = webView.title ?: ""
        }
    }

    fun getActiveWebView(): WebView? = activeTabId?.let { webViews[it] }

    fun loadUrl(url: String, tabId: String) {
        val webView = webViews[tabId] ?: createWebView(tabId)
        webView.loadUrl(url)
        _currentUrl.value = url
        _isLoading.value = true
        _loadingProgress.value = 0
    }

    fun goBack(tabId: String) {
        webViews[tabId]?.goBack()
    }

    fun goForward(tabId: String) {
        webViews[tabId]?.goForward()
    }

    fun reload(tabId: String) {
        webViews[tabId]?.reload()
    }

    fun stopLoading(tabId: String) {
        webViews[tabId]?.stopLoading()
    }

    fun clearError() {
        _error.value = null
    }
}
