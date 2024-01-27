package de.dercodeling.pizzacounter.ui.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListViewModel
import de.dercodeling.pizzacounter.ui.screens.main.MainScreen
import de.dercodeling.pizzacounter.ui.screens.settings.SettingsScreen

@Composable
fun Navigation(
    viewModel: PizzaListViewModel
) {
    val navController = rememberNavController()

    val onNavigateToSettings: () -> Unit = {
        navController.navigate(Screen.SettingsScreen.route)
    }

    val onNavigateUpFromSettings: () -> Unit = {
        if (navController.previousBackStackEntry != null)
            navController.navigateUp()
        else
            navController.navigate(Screen.MainScreen.route)
    }

    NavHost(navController, startDestination = Screen.SettingsScreen.route) {
        composable(Screen.MainScreen.route) {
            val state by viewModel.state.collectAsState()

            MainScreen(state, viewModel::onEvent, onNavigateToSettings)
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(onNavigateUpFromSettings)
        }
    }

}