package dev.agentbrowser.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNav(
    onBrowserClick: () -> Unit,
    onTabsClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onBrowserClick) {
            Icon(Icons.Default.Refresh, contentDescription = "Browser")
        }
        IconButton(onClick = onTabsClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Tabs")
        }
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Menu")
        }
    }
}
