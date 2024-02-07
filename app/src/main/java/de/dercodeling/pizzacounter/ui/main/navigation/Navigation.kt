package de.dercodeling.pizzacounter.ui.main.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.dercodeling.pizzacounter.ui.screens.main.MainScreen
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListState
import de.dercodeling.pizzacounter.ui.screens.settings.SettingsScreen

@Composable
fun Navigation(
    context: Context,
    state: PizzaListState,
    onEvent: (PizzaListEvent) -> Unit
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

    NavHost(navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {
            MainScreen(state, onEvent, onNavigateToSettings)
        }
        composable(
            Screen.SettingsScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            SettingsScreen(context, onNavigateUpFromSettings)
        }
    }

}