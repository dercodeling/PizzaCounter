package de.dercodeling.pizzacounter.ui.screens.main.viewmodel

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.dercodeling.pizzacounter.data.local.PizzaTypeDao
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PizzaListViewModel(
    private val dao: PizzaTypeDao
): ViewModel() {

    private var initialTypes = MutableStateFlow(
        listOf(
            "Margherita",
            "Prosciutto",
            "Salami"/*,
            "Tonno e cipolla",
            "Regina",
            "Dalla casa",
            "Arrabiata",
            "Funghi",
            "Quattro stagioni"*/
        )
    )

    private val _sortType = MutableStateFlow(SortType.NAME)

    private val _pizzaTypes = _sortType
        .flatMapLatest {sortType ->
            when(sortType) {
                SortType.NAME -> dao.getPizzaTypesOrderedByName()
                SortType.QUANTITY_ASC -> dao.getPizzaTypesOrderedByQuantityAscending()
                SortType.QUANTITY_DESC -> dao.getPizzaTypesOrderedByQuantityDescending()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(PizzaListState())
    val state = combine(_state, _sortType, _pizzaTypes) {state, sortType, pizzaTypes ->
        state.copy(
            pizzaTypes = pizzaTypes,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PizzaListState())


    fun onEvent(event: PizzaListEvent) {
        when(event) {
            PizzaListEvent.LoadInitialPizzaTypes -> {
                viewModelScope.launch {
                    for (initialType in initialTypes.value) {
                        dao.insertPizzaType(PizzaType(initialType,0))
                    }
                }
            }

            is PizzaListEvent.AddPizzaType -> {
                viewModelScope.launch {
                    dao.insertPizzaType(event.pizzaType)
                }
            }

            is PizzaListEvent.DeletePizzaType -> {
                viewModelScope.launch {
                    dao.deletePizzaType(event.pizzaType)
                }
            }

            is PizzaListEvent.IncreaseQuantity -> {
                viewModelScope.launch {
                    val current = event.pizzaType
                    dao.updatePizzaType(current.copy(quantity = current.quantity+1))
                }
            }

            is PizzaListEvent.DecreaseQuantity -> {
                viewModelScope.launch {
                    val current = event.pizzaType
                    if (current.quantity > 0) {
                        dao.updatePizzaType(current.copy(quantity = current.quantity-1))
                    }
                }
            }

            is PizzaListEvent.SetSortType -> {
                _sortType.value = event.sortType
            }

            PizzaListEvent.ResetQuantities -> {
                viewModelScope.launch {
                    dao.clearQuantities()
                }
            }

            PizzaListEvent.ResetTypes -> {
                viewModelScope.launch {
                    dao.clearTypes()
                    for (initialType in initialTypes.value) {
                        dao.insertPizzaType(PizzaType(initialType,0))
                    }
                }
            }

            is PizzaListEvent.ShareList -> {
                viewModelScope.launch {
                    var outString = event.shareStringPrefix

                    for (pizzaType in _pizzaTypes.value) {
                        if(pizzaType.quantity>0) outString += "\n${pizzaType.quantity}× ${pizzaType.name}"
                    }

                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, outString)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(event.context, shareIntent, null)
                }
            }
        }
    }
}