package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.ui.theme.makeDeemphasizedVariant

@Composable
fun ToggleSetting (
    heading: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(15.dp),
    label: String = "",
) {
    Box (
        modifier.clickable {
            onCheckedChange(!isChecked)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(contentPadding)
        ) {
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    heading,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (label.isNotEmpty()) {
                    Text(
                        label,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.makeDeemphasizedVariant()
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}