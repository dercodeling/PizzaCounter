package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SouthEast
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterEvent
import de.dercodeling.pizzacounter.ui.theme.makeDeemphasizedVariant

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzaList(
    pizzaTypes: List<PizzaType>,
    onEvent: (PizzaCounterEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isCompactLayout by remember { mutableStateOf(false) }

    LazyColumn(
        modifier.fillMaxWidth(),
        contentPadding = PaddingValues(15.dp,20.dp),
    ) {
        items(
            items = pizzaTypes,
            key = { it.name }
        ) { pizzaType ->
            val otherPizzaTypes = pizzaTypes.filter { it != pizzaType }

            PizzaListItem(
                pizzaType,
                otherPizzaTypes = otherPizzaTypes,
                onEvent,
                onCompressLayout = { isCompactLayout = true },
                isCompactLayout,
                Modifier.animateItemPlacement() // Doesn't seem to do anything
            )
        }
    }

    if (pizzaTypes.isEmpty()) {
        val hintColor = MaterialTheme.colorScheme.onSurface.makeDeemphasizedVariant()

        Column (
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.empty_list_hint),
                Modifier.padding(50.dp, 20.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = hintColor
            )
            Icon(
                Icons.Rounded.SouthEast,
                null,
                Modifier.size(30.dp),
                tint = hintColor
            )
        }
    }
}

@Preview
@Composable
fun PizzaListPreview() {
    val pizzaTypes = listOf(
        PizzaType("Margherita",2),
        PizzaType("Salami", 8),
        PizzaType("Tonno", 4)
    )
    val onEvent: (PizzaCounterEvent) -> Unit = { _ -> }

    Box (
        Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        PizzaList(pizzaTypes = pizzaTypes, onEvent = onEvent)
    }
}