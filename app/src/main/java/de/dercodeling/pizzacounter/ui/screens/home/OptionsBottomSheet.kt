package de.dercodeling.pizzacounter.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OptionsBottomSheet(
    onDismiss: (BottomSheetOption?) -> Unit,
    heading: String,
    optionsMap: Map<BottomSheetOption,String>,
    optionRow: @Composable() (
        bottomSheetOption: Pair<BottomSheetOption,String>,
        (BottomSheetOption?) -> Unit
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val closeAndApply: (sheetOption: BottomSheetOption?) -> Unit = { sheetOption ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss(sheetOption)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { closeAndApply(null) },
        sheetState = sheetState,
        dragHandle = {}
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(30.dp)
        ) {
            Column(verticalArrangement = Arrangement.SpaceEvenly) {

                if(heading.isNotEmpty()) Text(heading) // TODO: Stylize (e.g. like in Google Tasks)

                for (option in optionsMap) {
                    optionRow(option.toPair(), closeAndApply)
                }
            }
        }
    }
}