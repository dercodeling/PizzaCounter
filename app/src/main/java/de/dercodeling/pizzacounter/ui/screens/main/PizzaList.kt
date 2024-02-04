package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListState

@Composable
fun PizzaList(
    state: PizzaListState,
    onEvent: (PizzaListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isCompactLayout by remember { mutableStateOf(false) }

    LazyColumn(
        modifier.fillMaxWidth(),
        contentPadding = PaddingValues(15.dp,20.dp),
    ) {
        items(
            items = state.pizzaTypes,
            key = { it.name }
        ) { pizzaType ->
            PizzaListItem(pizzaType, onEvent, { isCompactLayout = true }, isCompactLayout)
        }
    }
}

@Preview
@Composable
fun PizzaListPreview() {
    val state = PizzaListState(
        pizzaTypes = listOf(
            PizzaType("Margherita",2),
            PizzaType("Salami", 8),
            PizzaType("Tonno", 4)
        )
    )
    val onEvent: (PizzaListEvent) -> Unit = { _ -> }

    Box (
        Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        PizzaList(state = state, onEvent = onEvent)
    }
}