package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.dercodeling.pizzacounter.R

@Composable
fun ResetQuantitiesDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if(visible) {
        AlertDialog(
            title = {
                Text(text = stringResource(R.string.reset_quantities_dialog_header))
            },
            text = {
                Text(text = stringResource(R.string.reset_quantities_dialog_text))
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(stringResource(R.string.reset_option_quantities))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        )
    }
}

@Composable
fun ResetTypesDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (visible) {
        AlertDialog(
            title = {
                Text(text = stringResource(R.string.reset_types_dialog_header))
            },
            text = {
                Text(text = stringResource(R.string.reset_types_dialog_text))
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(stringResource(R.string.reset_option_types))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        )
    }
}