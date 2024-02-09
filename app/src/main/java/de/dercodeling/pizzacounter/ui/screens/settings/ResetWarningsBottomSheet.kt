package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.ui.screens.components.BottomSheetHeading
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ResetWarningsBottomSheet(
    currentShowResetQuantitiesWarning: Boolean,
    currentShowResetTypesWarning: Boolean,
    onDismiss: (Boolean, Boolean) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var isClearQuantitiesWarningEnabled by remember { mutableStateOf(currentShowResetQuantitiesWarning) }
    var isClearTypesWarningEnabled by remember { mutableStateOf(currentShowResetTypesWarning) }

    val closeAndApply: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss(isClearQuantitiesWarningEnabled, isClearTypesWarningEnabled)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { closeAndApply() },
        sheetState = sheetState,
        dragHandle = {},
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(30.dp)
        ) {
            BottomSheetHeading(stringResource(R.string.setting_reset_warnings))

            Text(
                stringResource(R.string.setting_explanation_reset_warnings),
                Modifier.padding(0.dp, 10.dp, 0.dp, 20.dp)
            )

            val toggleSettingPadding = PaddingValues(25.dp, 0.dp)

            ToggleSetting(
                heading = stringResource(R.string.setting_reset_quantities_warning),
                isChecked = isClearQuantitiesWarningEnabled,
                 onCheckedChange = {
                    isClearQuantitiesWarningEnabled = it
                },
                modifier = Modifier.clip(RoundedCornerShape(40)),
                contentPadding = toggleSettingPadding,
            )

            ToggleSetting(
                heading = stringResource(R.string.setting_reset_types_warning),
                isChecked = isClearTypesWarningEnabled,
                onCheckedChange = {
                    isClearTypesWarningEnabled = it
                },
                modifier = Modifier.clip(RoundedCornerShape(40)),
                contentPadding = toggleSettingPadding,
            )

            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        closeAndApply()
                    },
                ) {
                    Text(stringResource(R.string.button_save))
                }
            }
        }
    }
}