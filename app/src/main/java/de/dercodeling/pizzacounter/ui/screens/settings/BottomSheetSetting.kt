package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.ui.theme.makeDeemphasizedVariant

@Composable
fun BottomSheetSetting (
    heading: String,
    label: String,
    bottomSheet: @Composable (() -> Unit) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showBottomSheet = true
            }
    ) {

        Column (Modifier.padding(15.dp)) {
            Text(
                heading,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (label.isNotEmpty()) {
                Text(
                    label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.makeDeemphasizedVariant()
                )
            }
        }
    }

    val onDismiss = {
        showBottomSheet = false
    }

    if (showBottomSheet) {
        bottomSheet(onDismiss)
    }
}