package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.LanguageSetting
import de.dercodeling.pizzacounter.ui.main.navigation.Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showInfoDialog by remember { mutableStateOf(false) }
    var showLanguageBottomSheet by remember { mutableStateOf(false) }
    var showThemeBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Settings",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.MainScreen.route) }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.button_back))
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(Icons.Outlined.Info, stringResource(R.string.button_about))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn (
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),

            contentPadding = PaddingValues(20.dp)
        ){
            item {
                SettingsGroup ("Appearance") {

                    // Make clickable area like in Lawnchair settings that opens OptionsBottomSheet for language, for theme, etc.

                    Text("Language")

                    val options = mapOf(
                        Pair(LanguageSetting.EN, stringResource(R.string.language_setting_en)),
                        Pair(LanguageSetting.DE, stringResource(R.string.language_setting_de))
                    )

                    for (option in options) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = false, onClick = { /*TODO*/ })
                            Text(
                                option.value
                            )
                        }
                    }
                }

                SettingsGroup("Behavior") {}
            }
        }

        val onDismissInfoDialog = {
            showInfoDialog = false
        }


        if (showInfoDialog) {
            AlertDialog(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                text = {
                    Text(text = stringResource(R.string.info_dialog))
                },
                onDismissRequest = {
                    onDismissInfoDialog()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // TODO: Open link to GitHub page
                            onDismissInfoDialog()
                        }
                    ) {
                        Text(stringResource(R.string.dialog_github))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissInfoDialog()
                        }
                    ) {
                        Text(stringResource(R.string.dialog_close))
                    }
                }
            )
        }
    }
}