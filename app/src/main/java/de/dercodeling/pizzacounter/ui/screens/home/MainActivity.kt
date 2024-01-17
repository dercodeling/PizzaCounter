package de.dercodeling.pizzacounter.ui.screens.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.dercodeling.pizzacounter.data.local.PizzaListDatabase
import de.dercodeling.pizzacounter.ui.screens.home.viewmodel.PizzaListViewModel
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        PizzaListDatabase.getDatabase(context = applicationContext)
    }
    private val viewModel by viewModels<PizzaListViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PizzaListViewModel(db.getDao()) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            // Create PizzaCounterTheme with or without darkMode-Boolean
            // depending on whether system theming is followed

            val state by viewModel.state.collectAsState()
            val themeSetting = state.themeSetting

            if (themeSetting.isFollowSystem) {
                PizzaCounterTheme {
                    MainScreen(state, viewModel::onEvent)
                }
            } else {
                PizzaCounterTheme(darkTheme = themeSetting.isDark) {
                    MainScreen(state, viewModel::onEvent)
                }
            }
        }
    }
}