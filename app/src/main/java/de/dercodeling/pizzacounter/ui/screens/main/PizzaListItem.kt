package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.ui.screens.components.SwipeToDeleteContainer
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterEvent
import de.dercodeling.pizzacounter.ui.screens.components.BottomSheetHeading
import de.dercodeling.pizzacounter.ui.theme.makeDeemphasizedVariant
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzaListItem(
    pizzaType: PizzaType,
    otherPizzaTypes: List<PizzaType>,
    onEvent: (PizzaCounterEvent) -> Unit,
    onCompressLayout: () -> Unit,
    isCompactLayout: Boolean,
    modifier: Modifier = Modifier
) {
    val onSurfaceDisabled = MaterialTheme.colorScheme.onSurface.makeDeemphasizedVariant()

    val outlinePadding = if(isCompactLayout) PaddingValues(20.dp, 10.dp) else PaddingValues(25.dp, 15.dp)
    val separatorPadding = if(isCompactLayout) 10.dp else 15.dp

    val haptics = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    var showFullNotes by remember { mutableStateOf(false) }
    val showFullNotesIfNotEmpty = showFullNotes && pizzaType.notes.isNotEmpty()
    var showEditNotesBottomSheet by remember { mutableStateOf(false) }

    SwipeToDeleteContainer(
        item = pizzaType,
        onDelete = {
            onEvent(PizzaCounterEvent.DeletePizzaType(pizzaType, showSnackbar = true))
        },
        modifier = modifier.padding(0.dp, 8.dp)
    ) {
        OutlinedCard(
            Modifier
                .padding(0.dp, 8.dp)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        showFullNotes = !showFullNotes
                    },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        showEditNotesBottomSheet = true
                    },
                    onLongClickLabel = stringResource(R.string.long_press_label_edit_notes)
                )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(outlinePadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            // Quantity
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
                        Column {
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
                            if(pizzaType.notes.isNotEmpty()) {
                                AnimatedVisibility(visible = !showFullNotesIfNotEmpty) {
                                    Text(
                                        pizzaType.notes,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
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
                            onClick = { onEvent(PizzaCounterEvent.IncreaseQuantity(pizzaType)) },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                stringResource(R.string.button_increase),
                                tint = onButtonColor
                            )
                        }

                        Button(
                            onClick = { onEvent(PizzaCounterEvent.DecreaseQuantity(pizzaType)) },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Icon(
                                Icons.Rounded.Remove,
                                stringResource(R.string.button_decrease),
                                tint = onButtonColor
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = showFullNotesIfNotEmpty) {
                    Text(
                        pizzaType.notes
                    )
                }
            }
        }
    }

    if(showEditNotesBottomSheet) {
        EditNotesBottomSheet(
            pizzaType,
            otherPizzaTypes.map { it.name },
            onDismiss = {newPizzaType ->
                showEditNotesBottomSheet = false

                if(newPizzaType != null && newPizzaType != pizzaType) {
                    if(newPizzaType.name == pizzaType.name) {
                        onEvent(PizzaCounterEvent.UpdatePizzaType(newPizzaType))
                    } else {
                        onEvent(PizzaCounterEvent.DeletePizzaType(pizzaType, showSnackbar = false))
                        onEvent(PizzaCounterEvent.AddPizzaType(newPizzaType))
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNotesBottomSheet(
    pizzaType: PizzaType,
    otherPizzaTypeNames: List<String>,
    onDismiss: (PizzaType?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var nameTextFieldValue by remember { mutableStateOf(pizzaType.name) }
    var quantityTextFieldValue by remember { mutableStateOf(pizzaType.quantity.toString()) }
    var notesTextFieldValue by remember { mutableStateOf(pizzaType.notes) }

    val nameAlreadyExists = otherPizzaTypeNames.contains(nameTextFieldValue)
    val nameIsEmpty = nameTextFieldValue.isEmpty()
    val nameIsValid = !nameAlreadyExists && !nameIsEmpty
    val quantityIsValid =
        try {
            quantityTextFieldValue.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }

    val closeAndSave: (save: Boolean) -> Unit = { save ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss(
                    if(save) PizzaType(
                        name = nameTextFieldValue,
                        quantity = quantityTextFieldValue.toInt(),
                        notes = notesTextFieldValue
                    ) else null
                )
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            closeAndSave(false)
        },
        sheetState = sheetState,
        dragHandle = {},
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column (
            modifier = Modifier.padding(30.dp),
            horizontalAlignment = Alignment.End
        ) {
            Column {
                BottomSheetHeading(
                    stringResource(R.string.heading_edit),
                    Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
                )

                @Composable
                fun InvalidInputWarning(warning: String) {
                    Text(
                        warning,
                        color = Color.Red.makeDeemphasizedVariant()
                    )
                }

                if (nameAlreadyExists) {
                    InvalidInputWarning(stringResource(R.string.edit_name_already_exists))
                } else if (nameIsEmpty) {
                    InvalidInputWarning(stringResource(R.string.edit_name_is_empty))
                }

                if (!quantityIsValid) {
                    InvalidInputWarning(stringResource(R.string.edit_quantity_is_not_valid))
                }

                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(0.dp, 10.dp)
                ) {
                    OutlinedTextField(
                        value = nameTextFieldValue,
                        label = { Text(stringResource(R.string.label_edit_name)) },
                        onValueChange = { nameTextFieldValue = it},

                        singleLine = true,
                        isError = !nameIsValid,

                        colors = OutlinedTextFieldDefaults.colors(
                            errorLabelColor = Color.Red.makeDeemphasizedVariant(),
                            errorBorderColor = Color.Red.makeDeemphasizedVariant()
                        ),

                        modifier = Modifier.weight(3f)
                    )

                    OutlinedTextField(
                        value = quantityTextFieldValue,
                        label = { Text(stringResource(R.string.label_edit_quantity)) },
                        onValueChange = { newValue ->
                            val trimmedValue = newValue.trimStart{ it == '0' }

                            quantityTextFieldValue = trimmedValue.ifEmpty { "0" }
                        },

                        isError = !quantityIsValid,

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),

                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = notesTextFieldValue,
                    label = { Text(stringResource(R.string.label_edit_notes)) },
                    onValueChange = { notesTextFieldValue = it },

                    modifier = Modifier.fillMaxWidth()
                )
            }

            TextButton(
                onClick = { closeAndSave(true) },
                enabled = nameIsValid && quantityIsValid
            ) {
                Text(stringResource(R.string.button_save))
            }
        }
    }
}

@Preview
@Composable
fun PizzaListItemPreview() {
    val name = "Margherita"
    val quantity = 4

    val pizzaType = PizzaType(name, quantity)
    val pizzaTypeWithNotes = PizzaType(name, quantity, "Notes that are too long for one line")

    Column {
        Text("Normal", color = Color.Gray)
        PizzaListItem(pizzaType,emptyList(),{},{},false)

        Text("Normal with notes", color = Color.Gray)
        PizzaListItem(pizzaTypeWithNotes,emptyList(),{},{},false)

        Text("Compressed", color = Color.Gray)
        PizzaListItem(pizzaType,emptyList(),{},{},true)

        Text("Compressed with notes", color = Color.Gray)
        PizzaListItem(pizzaTypeWithNotes,emptyList(),{},{},true)
    }
}