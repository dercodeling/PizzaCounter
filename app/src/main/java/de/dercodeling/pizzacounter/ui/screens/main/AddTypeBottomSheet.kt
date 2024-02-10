package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.PizzaType
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddTypeBottomSheet(currentPizzaTypes: List<PizzaType>, onDismiss: (PizzaType?) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        sheetState = sheetState,
        dragHandle = {},
        windowInsets = WindowInsets(0,0,0,0)
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            var textFieldValue by remember { mutableStateOf("") }
            val textFieldFocusRequester = FocusRequester()
            //val focusManager = LocalFocusManager.current

            fun closeAndAddPizzaType() {
                //focusManager.clearFocus()

                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss(PizzaType(textFieldValue, 0))
                    }
                }
            }

            TextField(
                value = textFieldValue,
                label = { Text(stringResource(R.string.label_new_type)) },

                singleLine = true,
                onValueChange = { textFieldValue = it },

                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(textFieldFocusRequester)
                    .padding(10.dp),

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),

                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),

                keyboardActions = KeyboardActions(
                    onDone = {
                        closeAndAddPizzaType()
                    }
                )
            )

            LaunchedEffect(Unit) {
                textFieldFocusRequester.requestFocus()
            }

            val currentPizzaTypeNames = currentPizzaTypes.map { pizzaType -> pizzaType.name }

            TextButton(
                onClick = {
                    closeAndAddPizzaType()
                },
                enabled = textFieldValue.isNotEmpty() && !currentPizzaTypeNames.contains(textFieldValue)
            ) {
                Text(stringResource(R.string.button_add))
            }
        }
    }
}