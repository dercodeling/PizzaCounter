package de.dercodeling.pizzacounter.ui.main.viewmodel

import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import de.dercodeling.pizzacounter.domain.model.LanguageOption
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.domain.model.ThemeOption

sealed interface PizzaCounterEvent {
    data class SetWindowSizeClass(val windowSizeClass: WindowSizeClass): PizzaCounterEvent

    data class ReloadNewestVersionInfo(val currentVersionNumber: String?): PizzaCounterEvent

    // Settings
    data class SetLanguage(val languageOption: LanguageOption): PizzaCounterEvent
    data class SetTheme(val themeOption: ThemeOption): PizzaCounterEvent
    data class SetDefaultTypes(val names: List<String>): PizzaCounterEvent
    data class SetShowResetQuantitiesWarning(val showWarning: Boolean): PizzaCounterEvent
    data class SetShowResetTypesWarning(val showWarning: Boolean): PizzaCounterEvent

    // PizzaList
    data object LoadDefaultPizzaTypes: PizzaCounterEvent
    data class AddPizzaType(val pizzaType: PizzaType): PizzaCounterEvent
    data class DeletePizzaType(val pizzaType: PizzaType, val showSnackbar: Boolean): PizzaCounterEvent
    data class UpdatePizzaType(val pizzaType: PizzaType): PizzaCounterEvent
    data class IncreaseQuantity(val pizzaType: PizzaType): PizzaCounterEvent
    data class DecreaseQuantity(val pizzaType: PizzaType): PizzaCounterEvent
    data class SetSortType(val sortType: SortType): PizzaCounterEvent
    data object ResetQuantities: PizzaCounterEvent
    data object ResetTypes: PizzaCounterEvent
    data class ShareList(val shareStringPrefix: String, val context: Context): PizzaCounterEvent
}