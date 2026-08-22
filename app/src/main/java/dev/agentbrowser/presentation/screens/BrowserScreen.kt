package dev.agentbrowser.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.agentbrowser.presentation.viewmodel.BrowserViewModel
import dev.agentbrowser.presentation.components.AddressBar
import dev.agentbrowser.platform.WebViewEngine

@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel,
    engine: WebViewEngine,
    onNavigateToTabs: () -> Unit
) {
    val state by viewModel.browserState.collectAsState()
    val tabs by viewModel.tabs.collectAsState()
    val activeWebView = engine.getActiveWebView()
    var urlInput by remember { mutableStateOf(state.currentUrl) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            AddressBar(
                url = urlInput,
                onUrlChange = { urlInput = it },
                onUrlSubmit = {
                    urlInput = it
                    viewModel.loadUrl(it)
                },
                onTabClick = onNavigateToTabs,
                tabCount = tabs.size
            )

            if (state.isLoading) {
                LinearProgressIndicator(
                    progress = state.loadingProgress / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            state.error?.let { error ->
                ErrorState(
                    message = error,
                    onRetry = {
                        viewModel.clearError()
                        viewModel.loadUrl(state.currentUrl)
                    }
                )
            }

            if (activeWebView != null) {
                androidx.compose.ui.viewinterop.AndroidView(
                    factory = { activeWebView },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 8.dp)) {
            Text("Retry")
        }
    }
}
