package dev.agentbrowser.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BrowserScaffold(
    title: String,
    url: String,
    isLoading: Boolean,
    loadingProgress: Int,
    canGoBack: Boolean,
    canGoForward: Boolean,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onRefresh: () -> Unit,
    onStopLoading: () -> Unit,
    onNewTab: () -> Unit,
    onTabSwitch: () -> Unit,
    tabCount: Int,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    IconButton(onClick = onBack, enabled = canGoBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(onClick = onForward, enabled = canGoForward) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
                    }
                    if (isLoading) {
                        IconButton(onClick = onStopLoading) {
                            Icon(Icons.Default.Close, contentDescription = "Stop")
                        }
                    } else {
                        IconButton(onClick = onRefresh) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
                Row {
                    IconButton(onClick = onNewTab) {
                        Icon(Icons.Default.Refresh, contentDescription = "New Tab")
                    }
                    IconButton(onClick = onTabSwitch) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tabs")
                    }
                }
            }

            content()
        }
    }
}
