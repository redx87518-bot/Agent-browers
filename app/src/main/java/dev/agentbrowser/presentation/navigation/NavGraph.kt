package dev.agentbrowser.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.agentbrowser.presentation.screens.BrowserScreen
import dev.agentbrowser.presentation.screens.PlaceholderScreen
import dev.agentbrowser.presentation.viewmodel.BrowserViewModel
import dev.agentbrowser.presentation.viewmodel.TabsViewModel
import dev.agentbrowser.platform.WebViewEngine

@Composable
fun NavGraph(
    navController: NavHostController,
    browserViewModel: BrowserViewModel,
    tabsViewModel: TabsViewModel,
    engine: WebViewEngine
) {
    NavHost(navController = navController, startDestination = "browser") {
        composable("browser") {
            BrowserScreen(
                viewModel = browserViewModel,
                engine = engine
            )
        }
        composable("menu") {
            PlaceholderScreen(
                title = "Menu",
                message = "Coming in a future phase"
            )
        }
        composable("agent") {
            PlaceholderScreen(
                title = "AI Agent",
                message = "Coming in a future phase"
            )
        }
        composable("tasks") {
            PlaceholderScreen(
                title = "Tasks",
                message = "Coming in a future phase"
            )
        }
    }
}
