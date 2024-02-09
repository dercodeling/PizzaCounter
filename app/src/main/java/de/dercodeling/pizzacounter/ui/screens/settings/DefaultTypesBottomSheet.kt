package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.ui.screens.components.BottomSheetHeading
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultTypesBottomSheet(
    currentValue: List<String>,
    onDismiss: (String?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var input by remember { mutableStateOf( currentValue.joinToString(", ")) }

    val closeAndApply: (String?) -> Unit = { inputString ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss(inputString)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { closeAndApply(null) },
        sheetState = sheetState,
        dragHandle = {},
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(30.dp)
        ) {
            BottomSheetHeading(stringResource(R.string.setting_default_types))

            Text(
                stringResource(R.string.setting_explanation_default_types),
                Modifier.padding(0.dp, 10.dp, 0.dp, 20.dp)
            )

            // Note: Unlike in AddTypeBottomSheet focus is not immediately requested for this
            // text field in order to let the user read the explanations above and because
            // it's less necessary here and therefore not worth the ugly opening animation
            OutlinedTextField(
                placeholder = { Text(stringResource(R.string.setting_default_types_input_label)) },
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        closeAndApply(input)
                    }
                )
            )

            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        closeAndApply(input)
                    },
                ) {
                    Text(stringResource(R.string.button_save))
                }
            }
        }
    }
}