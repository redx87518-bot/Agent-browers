package dev.agentbrowser.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.agentbrowser.presentation.screens.BrowserScreen
import dev.agentbrowser.presentation.screens.NewTabPage
import dev.agentbrowser.presentation.screens.PlaceholderScreen
import dev.agentbrowser.presentation.screens.TabsScreen
import dev.agentbrowser.presentation.viewmodel.BrowserViewModel
import dev.agentbrowser.presentation.viewmodel.TabsViewModel
import dev.agentbrowser.platform.GeckoEngine

@Composable
fun NavGraph(
    navController: NavHostController,
    browserViewModel: BrowserViewModel,
    tabsViewModel: TabsViewModel,
    engine: GeckoEngine
) {
    NavHost(navController = navController, startDestination = "browser") {
        composable("browser") {
            BrowserScreen(
                viewModel = browserViewModel,
                engine = engine,
                onNavigateToTabs = { navController.navigate("tabs") }
            )
        }
        composable("tabs") {
            TabsScreen(
                viewModel = tabsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("newtab") {
            NewTabPage { url ->
                browserViewModel.loadUrl(url)
                navController.navigate("browser")
            }
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
