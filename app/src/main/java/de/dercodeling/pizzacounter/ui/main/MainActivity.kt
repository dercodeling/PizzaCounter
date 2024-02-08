package de.dercodeling.pizzacounter.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.dercodeling.pizzacounter.data.local.PizzaCounterDatabase
import de.dercodeling.pizzacounter.ui.main.navigation.Navigation
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListViewModel
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        PizzaCounterDatabase.getDatabase(context = applicationContext)
    }
    private val viewModel by viewModels<PizzaListViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return PizzaListViewModel(db.getDao()) as T
                }
            }
        }
    )


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)



        setContent {
            val state by viewModel.state.collectAsState()

            val windowSizeClass = calculateWindowSizeClass(this)
            viewModel.onEvent(PizzaListEvent.SetWindowSizeClass(windowSizeClass))

            val themeSetting = state.theme

            PizzaCounterTheme(themeSetting) {
                Navigation(applicationContext, state, viewModel::onEvent)
            }
        }
    }
}