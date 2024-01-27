package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import de.dercodeling.pizzacounter.ui.screens.components.OptionsBottomSheet
import de.dercodeling.pizzacounter.domain.model.BottomSheetOption

@Composable
fun OptionsBottomSheetSetting (
    heading: String,
    optionsMap: Map<BottomSheetOption, String>,
    currentOption: BottomSheetOption,
    onSelected: (BottomSheetOption?) -> Unit
) {

    var currentLabel = optionsMap[currentOption]
    if (currentLabel == null) currentLabel = ""

    BottomSheetSetting(heading = heading, label = currentLabel) { onDismiss ->

        OptionsBottomSheet(
            onDismiss = { selected ->
                onDismiss()
                onSelected(selected)
            },
            heading = heading,
            optionsMap = optionsMap) { option, closeAndApply ->
            RadioButton(
                selected = option.first == currentOption,
                onClick = {
                    closeAndApply(option.first)
                })
        }
    }
}