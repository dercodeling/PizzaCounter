package de.dercodeling.pizzacounter.ui.main.navigation

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_screen")
    data object SettingsScreen : Screen("settings_screen")
}