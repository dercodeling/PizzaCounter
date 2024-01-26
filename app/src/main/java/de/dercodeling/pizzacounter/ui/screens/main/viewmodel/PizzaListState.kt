package de.dercodeling.pizzacounter.ui.screens.main.viewmodel

import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.domain.model.ThemeSetting

data class PizzaListState(
    val themeSetting: ThemeSetting = ThemeSetting(),
    val pizzaTypes: List<PizzaType> = emptyList(),
    val sortType: SortType = SortType.NAME,
    )
