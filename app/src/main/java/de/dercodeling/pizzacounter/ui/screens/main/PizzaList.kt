package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListState

@Composable
fun PizzaList(state: PizzaListState, onEvent: (PizzaListEvent) -> Unit, innerPadding: PaddingValues) {
    LazyColumn(
        Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentPadding = PaddingValues(15.dp,20.dp)

    ) {
        val types = state.pizzaTypes

        items(types.size) { i ->
            PizzaListItem(types[i], onEvent)
        }
    }
}