package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.BottomSheetOption
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.ui.screens.components.OptionsBottomSheet

@Composable
fun SortBottomSheet(
    visible: Boolean,
    currentSortType: SortType,
    onDismiss: (BottomSheetOption?) -> Unit
) {
    if (visible) {
        OptionsBottomSheet(
            onDismiss = onDismiss,
            heading = stringResource(id = R.string.heading_sort),
            optionsMap = mapOf(
                Pair(
                    SortType.NAME,
                    stringResource(R.string.sorting_option_name)
                ),
                Pair(
                    SortType.QUANTITY_ASC,
                    stringResource(R.string.sorting_option_quantity_ascending)
                ),
                Pair(
                    SortType.QUANTITY_DESC,
                    stringResource(R.string.sorting_option_quantity_descending)
                )
            )
        ) { option, closeAndApply ->
            RadioButton(
                selected = (currentSortType == option.first),
                onClick = {
                    closeAndApply(option.first)
                })
        }
    }
}