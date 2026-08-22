package dev.agentbrowser.presentation.components

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class BrowserShellBridge(
    private val onNavigateTo: (String) -> Unit = {},
    private val onGoBack: () -> Unit = {},
    private val onGoForward: () -> Unit = {},
    private val onReload: () -> Unit = {},
    private val onStopLoading: () -> Unit = {},
    private val onCreateNewTab: () -> Unit = {},
    private val onCloseTab: (String) -> Unit = {},
    private val onSwitchTab: (String) -> Unit = {},
    private val onRetry: () -> Unit = {}
) {
    @JavascriptInterface
    fun navigateTo(url: String) {
        Log.d("BrowserShellBridge", "navigateTo: $url")
        onNavigateTo(url)
    }

    @JavascriptInterface
    fun goBack() {
        Log.d("BrowserShellBridge", "goBack")
        onGoBack()
    }

    @JavascriptInterface
    fun goForward() {
        Log.d("BrowserShellBridge", "goForward")
        onGoForward()
    }

    @JavascriptInterface
    fun reload() {
        Log.d("BrowserShellBridge", "reload")
        onReload()
    }

    @JavascriptInterface
    fun stopLoading() {
        Log.d("BrowserShellBridge", "stopLoading")
        onStopLoading()
    }

    @JavascriptInterface
    fun createNewTab() {
        Log.d("BrowserShellBridge", "createNewTab")
        onCreateNewTab()
    }

    @JavascriptInterface
    fun closeTab(tabId: String) {
        Log.d("BrowserShellBridge", "closeTab: $tabId")
        onCloseTab(tabId)
    }

    @JavascriptInterface
    fun switchTab(tabId: String) {
        Log.d("BrowserShellBridge", "switchTab: $tabId")
        onSwitchTab(tabId)
    }

    @JavascriptInterface
    fun retry() {
        Log.d("BrowserShellBridge", "retry")
        onRetry()
    }
}

@Composable
fun BrowserShellWebView(
    bridge: BrowserShellBridge,
    onWebViewReady: (WebView) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val webView = remember { WebView(context) }

    LaunchedEffect(Unit) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.setBackgroundColor(0x00000000)
        webView.addJavascriptInterface(bridge, "BrowserBridge")
        webView.loadUrl("file:///android_asset/browser-ui/index.html")
        onWebViewReady(webView)
    }

    AndroidView(
        factory = { webView },
        modifier = modifier
    )
}
