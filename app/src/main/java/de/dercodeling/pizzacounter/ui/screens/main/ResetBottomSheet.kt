package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.BottomSheetOption
import de.dercodeling.pizzacounter.domain.model.ResetOption
import de.dercodeling.pizzacounter.ui.screens.components.OptionsBottomSheet
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent

@Composable
fun ResetBottomSheet(
    visible: Boolean,
    onDismiss: (BottomSheetOption?) -> Unit,
    onEvent: (PizzaListEvent) -> Unit
) {
    var showResetQuantitiesDialog by remember { mutableStateOf(false) }
    var showResetTypesDialog by remember { mutableStateOf(false) }

    if (visible) {
        OptionsBottomSheet(
            onDismiss = {
                when (it) {
                    ResetOption.RESET_QUANTITIES -> showResetQuantitiesDialog = true
                    ResetOption.RESET_TYPES -> showResetTypesDialog = true
                    else -> {}
                }
                onDismiss(it)
            },
            heading = "",
            optionsMap = mapOf(
                Pair(
                    ResetOption.RESET_QUANTITIES,
                    stringResource(R.string.reset_option_quantities)
                ),
                Pair(
                    ResetOption.RESET_TYPES,
                    stringResource(R.string.reset_option_types)
                )
            )
        )
    }

    ResetQuantitiesDialog(
        visible = showResetQuantitiesDialog,
        onDismiss = {
            showResetQuantitiesDialog = false
        },
        onConfirm = {
            onEvent(PizzaListEvent.ResetQuantities)
            showResetQuantitiesDialog = false
        }
    )

    ResetTypesDialog(
        visible = showResetTypesDialog,
        onDismiss = {
            showResetTypesDialog = false
        },
        onConfirm = {
            onEvent(PizzaListEvent.ResetTypes)
            showResetTypesDialog = false
        }
    )
}