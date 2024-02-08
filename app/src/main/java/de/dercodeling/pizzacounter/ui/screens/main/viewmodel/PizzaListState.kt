package de.dercodeling.pizzacounter.ui.screens.main.viewmodel

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import de.dercodeling.pizzacounter.domain.model.LanguageOption
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.domain.model.ThemeOption

data class PizzaListState (
    val windowSizeClass: WindowSizeClass? = null,
    val language: LanguageOption = LanguageOption.SYSTEM,
    val theme: ThemeOption = ThemeOption.SYSTEM,
    val pizzaTypes: List<PizzaType> = emptyList(),
    val sortType: SortType = SortType.NAME,
    )