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

    NavHost(navController = navController, startDestination = Screen.SettingsScreen.route) {
        composable(route = Screen.MainScreen.route) {
            val state by viewModel.state.collectAsState()

            MainScreen(state, viewModel::onEvent, navController)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController)
        }
    }

}