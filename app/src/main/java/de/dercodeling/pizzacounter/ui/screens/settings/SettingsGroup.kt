package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsGroup (heading: String, content: @Composable () -> Unit) {

    Column {
        if(heading.isNotEmpty()) {
            Text(heading,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(15.dp,0.dp,0.dp,0.dp),
            )
        }
        Card(
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 20.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(Modifier.padding(15.dp, 0.dp))
}

@Preview
@Composable
fun SettingsGroupPreview() {
    SettingsGroup("Heading") {
        BottomSheetSetting(heading = "Setting", label = "Option") {}
        SettingsDivider()
        BottomSheetSetting(heading = "Lorem ipsum", label = "Other") {}
    }
}