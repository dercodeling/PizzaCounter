package de.dercodeling.pizzacounter.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetHeading(
    text: String,
    modifier: Modifier = Modifier
){
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(5.dp)
    )
}

@Preview
@Composable
fun BottomSheetHeadingPreview() {
    BottomSheetHeading("Heading")
}