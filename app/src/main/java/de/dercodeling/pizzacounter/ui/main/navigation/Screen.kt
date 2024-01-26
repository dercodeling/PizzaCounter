package de.dercodeling.pizzacounter.ui.main.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object SettingsScreen : Screen("settings_screen")
}