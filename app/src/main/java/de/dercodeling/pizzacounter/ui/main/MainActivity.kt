package de.dercodeling.pizzacounter.ui.main

import android.app.LocaleManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.dercodeling.pizzacounter.data.local.PizzaCounterDatabase
import de.dercodeling.pizzacounter.ui.main.navigation.Navigation
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterEvent
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterState
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterViewModel
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        PizzaCounterDatabase.getDatabase(context = applicationContext)
    }
    private val viewModel by viewModels<PizzaCounterViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return PizzaCounterViewModel(db.getDao()) as T
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
            val appVersion = getAppVersion(applicationContext)

            setLocale(state)

            val windowSizeClass = calculateWindowSizeClass(this)
            viewModel.onEvent(PizzaCounterEvent.SetWindowSizeClass(windowSizeClass))

            viewModel.onEvent(PizzaCounterEvent.ReloadNewestVersionInfo(appVersion))

            val themeSetting = state.theme

            PizzaCounterTheme(themeSetting) {
                Navigation(state, viewModel::onEvent, appVersion)
            }
        }
    }

    private fun setLocale(state: PizzaCounterState) {
        val languageTag = state.language.toLanguageTag()

        if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = applicationContext.getSystemService(LocaleManager::class.java)

            if (languageTag == null) {
                localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
            } else {
                localeManager.applicationLocales = LocaleList.forLanguageTags(languageTag)
            }
        } else {
            val appLocale = LocaleListCompat.forLanguageTags(languageTag)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

    private fun getAppVersion(
        context: Context,
    ): String? {
        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName
            val packageInfo = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            packageInfo.versionName
        } catch (e: Exception) {
            null
        }
    }
}