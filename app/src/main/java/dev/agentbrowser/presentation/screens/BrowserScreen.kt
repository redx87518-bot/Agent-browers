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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.agentbrowser.presentation.viewmodel.BrowserViewModel
import dev.agentbrowser.platform.GeckoEngine

@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel,
    engine: GeckoEngine,
    onNavigateToTabs: () -> Unit
) {
    val state by viewModel.browserState.collectAsState()
    val tabs by viewModel.tabs.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            AddressBar(
                url = state.currentUrl,
                onUrlChange = { viewModel.loadUrl(it) },
                onTabClick = { onNavigateToTabs() },
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

            engine.getGeckoView().let { geckoView ->
                androidx.compose.ui.viewinterop.AndroidView(
                    factory = { geckoView },
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
