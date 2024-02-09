package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
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
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterEvent
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzaList(
    state: PizzaCounterState,
    onEvent: (PizzaCounterEvent) -> Unit,
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
            PizzaListItem(
                pizzaType,
                onEvent,
                onCompressLayout = { isCompactLayout = true },
                isCompactLayout,
                Modifier.animateItemPlacement() // Doesn't seem to do anything
            )
        }
    }
}

@Preview
@Composable
fun PizzaListPreview() {
    val state = PizzaCounterState(
        pizzaTypes = listOf(
            PizzaType("Margherita",2),
            PizzaType("Salami", 8),
            PizzaType("Tonno", 4)
        )
    )
    val onEvent: (PizzaCounterEvent) -> Unit = { _ -> }

    Box (
        Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        PizzaList(state = state, onEvent = onEvent)
    }
}