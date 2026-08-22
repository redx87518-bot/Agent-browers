package dev.agentbrowser.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.agentbrowser.presentation.components.BrowserShellBridge
import dev.agentbrowser.presentation.components.BrowserShellWebView
import dev.agentbrowser.presentation.viewmodel.BrowserViewModel
import dev.agentbrowser.platform.WebViewEngine

@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel,
    engine: WebViewEngine
) {
    val state by viewModel.browserState.collectAsState()
    val activeWebView = engine.getActiveWebView()

    Box(modifier = Modifier.fillMaxSize()) {
        if (activeWebView != null) {
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { activeWebView },
                modifier = Modifier.fillMaxSize()
            )
        }

        val shellBridge = remember {
            BrowserShellBridge(
                onNavigateTo = { url -> viewModel.loadUrl(url) },
                onGoBack = { viewModel.goBack() },
                onGoForward = { viewModel.goForward() },
                onReload = { viewModel.refresh() },
                onStopLoading = { viewModel.stopLoading() },
                onCreateNewTab = { viewModel.newTab() },
                onCloseTab = { tabId -> viewModel.closeTab(tabId) },
                onSwitchTab = { tabId -> viewModel.switchTab(tabId) },
                onRetry = {
                    viewModel.clearError()
                    viewModel.loadUrl(state.currentUrl)
                }
            )
        }

        BrowserShellWebView(
            bridge = shellBridge,
            modifier = Modifier.fillMaxSize()
        )
    }
}
