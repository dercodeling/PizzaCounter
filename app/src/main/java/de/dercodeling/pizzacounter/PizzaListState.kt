package de.dercodeling.pizzacounter

import de.dercodeling.pizzacounter.model.PizzaType
import de.dercodeling.pizzacounter.model.SortType
import de.dercodeling.pizzacounter.model.ThemeSetting

data class PizzaListState(
    val themeSetting: ThemeSetting = ThemeSetting(),
    val pizzaTypes: List<PizzaType> = emptyList(),
    val sortType: SortType = SortType.NAME,
    )
