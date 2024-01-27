package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.ui.screens.components.BottomSheetHeading
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultTypesBottomSheet(
    onDismiss: (String?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val closeAndApply: (String?) -> Unit = { typesEntered ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss(typesEntered)
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
            Text(stringResource(R.string.setting_explanation_default_types))

            // TODO: Create sheet content
        }
    }
}