package de.dercodeling.pizzacounter.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.ui.theme.Typography

@Composable
fun BottomSheetHeading(text: String){
    Text(
        text = text,
        style = Typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(5.dp)
    )
}

@Preview
@Composable
fun BottomSheetHeadingPreview() {
    BottomSheetHeading("Heading")
}