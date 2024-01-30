package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent
import de.dercodeling.pizzacounter.ui.theme.makeDeemphasizedVariant

@Composable
fun PizzaListItem(pizzaType: PizzaType, onEvent: (PizzaListEvent) -> Unit, onCompressLayout: () -> Unit, isCompactLayout: Boolean) {
    val onSurfaceDisabled = MaterialTheme.colorScheme.onSurface.makeDeemphasizedVariant()

    val outlinePadding = if(isCompactLayout) PaddingValues(20.dp, 10.dp) else PaddingValues(25.dp, 15.dp)
    val separatorPadding = if(isCompactLayout) 10.dp else 15.dp

    OutlinedCard(
        modifier = Modifier.padding(0.dp, 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(outlinePadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text( // Quantity
                    text = pizzaType.quantity.toString(),
                    color = if (pizzaType.quantity == 0) onSurfaceDisabled else MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = stringResource(R.string.list_item_quantity_separator),
                    color = /*if (isCompactLayout) Color.Red else*/ onSurfaceDisabled,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(separatorPadding)
                )
                Text( // Type
                    text = pizzaType.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        // This doesn't allow for layout to expand again if all overly long names get deleted,
                        // but that is intended in order to to hide the layout changes from the user
                        // and prevent unnecessary visual disturbances.
                        if (textLayoutResult.lineCount > 1) onCompressLayout()
                    }
                )
            }

            val buttonColor = MaterialTheme.colorScheme.primaryContainer
            val onButtonColor = MaterialTheme.colorScheme.onPrimaryContainer

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                Button(
                    modifier = Modifier.padding(5.dp),
                    onClick = { onEvent(PizzaListEvent.IncreaseQuantity(pizzaType)) },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(Icons.Rounded.Add, stringResource(R.string.button_increase), tint = onButtonColor)
                }

                Button(onClick = { onEvent(PizzaListEvent.DecreaseQuantity(pizzaType)) },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(Icons.Rounded.Remove, stringResource(R.string.button_decrease), tint = onButtonColor)
                }
            }
        }
    }
}

@Preview
@Composable
fun PizzaListItemPreview() {
    Column {
        Text("Normal", color = Color.Gray)
        PizzaListItem(PizzaType("Margherita", 4),{},{},false)

        Text("Compressed", color = Color.Gray)
        PizzaListItem(PizzaType("Margherita", 4),{},{},true)
    }
}