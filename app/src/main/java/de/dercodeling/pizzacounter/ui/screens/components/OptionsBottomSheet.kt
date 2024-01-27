package de.dercodeling.pizzacounter.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.domain.model.BottomSheetOption
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OptionsBottomSheet(
    onDismiss: (BottomSheetOption?) -> Unit,
    heading: String,
    optionsMap: Map<BottomSheetOption,String>,
    optionPrefix: (@Composable (
        bottomSheetOption: Pair<BottomSheetOption,String>,
        closeAndApply: (BottomSheetOption?) -> Unit
    ) -> Unit)? = null
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
        dragHandle = {},
        windowInsets = WindowInsets(0,0,0,0)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(30.dp)
        ) {
            Column(verticalArrangement = Arrangement.SpaceEvenly) {

                if(heading.isNotEmpty()) BottomSheetHeading(heading)

                for (option in optionsMap) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(40))
                            .clickable {
                                closeAndApply(option.key)
                            }
                    ) {
                        if (optionPrefix != null) {
                            optionPrefix(option.toPair(), closeAndApply)
                        } else {
                            Spacer(Modifier.padding(5.dp, 25.dp))
                        }
                        Text(option.value)
                    }

                }
            }
        }
    }
}