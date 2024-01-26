package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent

@Composable
fun PizzaListItem(pizzaType: PizzaType, onEvent: (PizzaListEvent) -> Unit) {
    OutlinedCard(
        modifier = Modifier.padding(0.dp, 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 15.dp) // TODO: Possibly reduce (only on small screens?) to prevent unnecessary line breaks
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text( // Quantity
                    text = pizzaType.quantity.toString(),
                    color = if (pizzaType.quantity == 0) Color.Gray else MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "×",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(15.dp)
                )
                Text( // Type
                    text = pizzaType.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                Button(
                    modifier = Modifier.padding(5.dp),
                    onClick = { onEvent(PizzaListEvent.IncreaseQuantity(pizzaType)) }
                ) {
                    Icon( Icons.Rounded.Add, stringResource(R.string.button_increase))
                }

                Button(onClick = { onEvent(PizzaListEvent.DecreaseQuantity(pizzaType)) }
                ) {
                    Icon( Icons.Rounded.Remove, stringResource(R.string.button_decrease))
                }
            }
        }
    }
}