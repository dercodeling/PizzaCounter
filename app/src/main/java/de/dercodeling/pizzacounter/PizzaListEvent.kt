package de.dercodeling.pizzacounter

import de.dercodeling.pizzacounter.model.PizzaType
import de.dercodeling.pizzacounter.model.SortType

sealed interface PizzaListEvent {
    data object LoadInitialPizzaTypes: PizzaListEvent
    data class AddPizzaType(val pizzaType: PizzaType): PizzaListEvent
    data class DeletePizzaType(val pizzaType: PizzaType): PizzaListEvent
    data class IncreaseQuantity(val pizzaType: PizzaType): PizzaListEvent
    data class DecreaseQuantity(val pizzaType: PizzaType): PizzaListEvent
    data class SetSortType(val sortType: SortType): PizzaListEvent
    data object ClearQuantities: PizzaListEvent
    data object ResetTypes: PizzaListEvent
}