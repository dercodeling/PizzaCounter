package de.dercodeling.pizzacounter.ui.screens.main.viewmodel

import android.content.Context
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.SortType

sealed interface PizzaListEvent {
    data object LoadInitialPizzaTypes: PizzaListEvent
    data class AddPizzaType(val pizzaType: PizzaType): PizzaListEvent
    data class DeletePizzaType(val pizzaType: PizzaType): PizzaListEvent
    data class IncreaseQuantity(val pizzaType: PizzaType): PizzaListEvent
    data class DecreaseQuantity(val pizzaType: PizzaType): PizzaListEvent
    data class SetSortType(val sortType: SortType): PizzaListEvent
    data object ResetQuantities: PizzaListEvent
    data object ResetTypes: PizzaListEvent
    data class ShareList(val shareStringPrefix: String, val context: Context): PizzaListEvent
}