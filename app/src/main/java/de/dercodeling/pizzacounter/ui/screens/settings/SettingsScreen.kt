package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.LanguageOption
import de.dercodeling.pizzacounter.domain.model.ThemeOption
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterEvent
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterState
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    state: PizzaCounterState,
    onEvent: (PizzaCounterEvent) -> Unit,
    onNavigateUp: () -> Unit,
    appVersion: String?,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showInfoDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.heading_settings),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.displayMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
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
                SettingsGroup (stringResource(R.string.settings_group_appearance)) {
                    val languageOptionsMap = mutableMapOf(
                        Pair(
                            LanguageOption.SYSTEM,
                            stringResource(R.string.settings_option_system)
                        )
                    )

                    for (option in LanguageOption.entries.filter { it != LanguageOption.SYSTEM }) {
                        languageOptionsMap[option] = option.toLanguageName()
                    }

                    OptionsBottomSheetSetting(
                        stringResource(R.string.setting_language),
                        languageOptionsMap.toMap(),
                        state.language,
                        onSelected = { bottomSheetOption ->
                            if(bottomSheetOption is LanguageOption)
                                onEvent(PizzaCounterEvent.SetLanguage(bottomSheetOption))
                        }
                    )

                    SettingsDivider()

                    OptionsBottomSheetSetting(
                        heading = stringResource(R.string.setting_theme),
                        optionsMap = mapOf(
                            Pair(
                                ThemeOption.SYSTEM,
                                stringResource(R.string.settings_option_system)
                            ),
                            Pair(
                                ThemeOption.LIGHT,
                                stringResource(R.string.theme_option_light)
                            ),Pair(
                                ThemeOption.DARK,
                                stringResource(R.string.theme_option_dark)
                            )
                        ),
                        currentOption = state.theme,
                        onSelected = { bottomSheetOption ->
                            if(bottomSheetOption is ThemeOption)
                                onEvent(PizzaCounterEvent.SetTheme(bottomSheetOption))
                        }
                    )
                }
            }

            item {
                SettingsGroup(stringResource(R.string.settings_group_behavior)) {
                    BottomSheetSetting(
                        heading = stringResource(R.string.setting_default_types),
                        label = stringResource(R.string.setting_label_default_types)
                    ) { onDismiss ->
                        DefaultTypesBottomSheet (
                            currentValue = state.defaultTypes,
                            onDismiss = { defaultTypesInput ->
                                if (defaultTypesInput != null) {
                                    val names = defaultTypesInput.split(",")
                                    val namesClean = names.map { name -> name.trim() }

                                    onEvent(PizzaCounterEvent.SetDefaultTypes(namesClean))
                                }

                                onDismiss()
                            }
                        )
                    }

                    SettingsDivider()

                    BottomSheetSetting(
                        heading = stringResource(R.string.setting_reset_warnings),
                        label = stringResource(R.string.setting_reset_warnings_label)
                    ) { onDismiss ->
                        ResetWarningsBottomSheet (
                            state.showResetQuantitiesWarning,
                            state.showResetTypesWarning,
                            onDismiss = { showResetQuantitiesWarning, showResetTypesWarning ->
                                onEvent(PizzaCounterEvent.SetShowResetQuantitiesWarning(showResetQuantitiesWarning))
                                onEvent(PizzaCounterEvent.SetShowResetTypesWarning(showResetTypesWarning))

                                onDismiss()
                            }
                        )
                    }
                }
            }
        }

        val onDismissInfoDialog = {
            showInfoDialog = false
        }

        if (showInfoDialog) {
            InfoDialog(appVersion, onDismissInfoDialog)
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@PreviewFontScale
@PreviewScreenSizes
@Composable
fun SettingsScreenPreview() {
    PizzaCounterTheme(themeSetting = ThemeOption.SYSTEM) {
        SettingsScreen(state = PizzaCounterState(), onEvent = {}, {}, appVersion = "x.y.z")
    }
}