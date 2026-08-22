package dev.agentbrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.rememberNavController
import dev.agentbrowser.presentation.navigation.NavGraph
import dev.agentbrowser.presentation.viewmodel.BrowserViewModel
import dev.agentbrowser.presentation.viewmodel.TabsViewModel
import dev.agentbrowser.platform.GeckoEngine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = application as BrowserApplication
        val engine = GeckoEngine(this)
        val browserViewModel = BrowserViewModel(application.browserRepository, application.tabManager)
        val tabsViewModel = TabsViewModel(application.tabManager, application.historyRepository)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        browserViewModel = browserViewModel,
                        tabsViewModel = tabsViewModel,
                        engine = engine
                    )
                }
            }
        }
    }
}
