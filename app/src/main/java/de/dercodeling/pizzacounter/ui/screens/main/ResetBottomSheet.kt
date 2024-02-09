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
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterEvent
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterState

@Composable
fun ResetBottomSheet(
    visible: Boolean,
    onDismiss: (BottomSheetOption?) -> Unit,
    state: PizzaCounterState,
    onEvent: (PizzaCounterEvent) -> Unit
) {
    var showResetQuantitiesDialog by remember { mutableStateOf(false) }
    var showResetTypesDialog by remember { mutableStateOf(false) }

    val onConfirmResetQuantities = {
        onEvent(PizzaCounterEvent.ResetQuantities)
    }

    val onConfirmResetTypes = {
        onEvent(PizzaCounterEvent.ResetTypes)
    }

    if (visible) {
        OptionsBottomSheet(
            onDismiss = {
                when (it) {
                    ResetOption.RESET_QUANTITIES -> {
                        if (state.showResetQuantitiesWarning) {
                            showResetQuantitiesDialog = true
                        } else {
                            onConfirmResetQuantities()
                        }
                    }
                    ResetOption.RESET_TYPES -> {
                        if (state.showResetTypesWarning) {
                            showResetTypesDialog = true
                        } else {
                            onConfirmResetTypes()
                        }
                    }
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
            onConfirmResetQuantities()
            showResetQuantitiesDialog = false
        }
    )

    ResetTypesDialog(
        visible = showResetTypesDialog,
        onDismiss = {
            showResetTypesDialog = false
        },
        onConfirm = {
            onConfirmResetTypes()
            showResetTypesDialog = false
        }
    )
}