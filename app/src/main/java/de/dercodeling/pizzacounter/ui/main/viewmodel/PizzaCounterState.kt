package de.dercodeling.pizzacounter.ui.main.viewmodel

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import de.dercodeling.pizzacounter.domain.model.LanguageOption
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.domain.model.ThemeOption

data class PizzaCounterState (
    val windowSizeClass: WindowSizeClass? = null,
    val language: LanguageOption = LanguageOption.SYSTEM,
    val theme: ThemeOption = ThemeOption.SYSTEM,
    val defaultTypes: List<String> = listOf("Margherita", "Prosciutto", "Salami"),
    val showResetQuantitiesWarning: Boolean = false,
    val showResetTypesWarning: Boolean = true,
    val sortType: SortType = SortType.NAME,
    val pizzaTypes: List<PizzaType> = emptyList(),
    )