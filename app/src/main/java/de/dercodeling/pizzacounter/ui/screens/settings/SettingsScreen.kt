package de.dercodeling.pizzacounter.ui.screens.settings

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.LanguageOption
import de.dercodeling.pizzacounter.domain.model.ThemeOption

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    context: Context,
    onNavigateUp: () -> Unit
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

                    OptionsBottomSheetSetting(
                        stringResource(R.string.setting_language),
                        mapOf(
                            Pair(
                                LanguageOption.SYSTEM,
                                stringResource(R.string.settings_option_system)
                            ),
                            Pair(
                                LanguageOption.EN,
                                stringResource(R.string.language_option_en)
                            ),
                            Pair(
                                LanguageOption.DE,
                                stringResource(R.string.language_option_de)
                            )
                        ),
                        LanguageOption.SYSTEM // TODO: Get actual current setting
                    ) {
                        // TODO: Save and apply
                    }

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
                        currentOption = ThemeOption.SYSTEM // TODO: Get actual current setting
                    ) {
                        // TODO: Save and apply
                    }
                }
            }

            item {
                SettingsGroup(stringResource(R.string.settings_group_behavior)) {
                    BottomSheetSetting(
                        heading = stringResource(R.string.setting_default_types),
                        label = stringResource(R.string.setting_label_default_types)
                    ) { onDismiss ->
                        DefaultTypesBottomSheet { _ -> //initialTypesInput ->
                            onDismiss()
                            // TODO: Save and apply
                        }
                    }

                    SettingsDivider()

                    BottomSheetSetting(
                        heading = stringResource(R.string.setting_reset_warnings),
                        label = stringResource(R.string.setting_reset_warnings_label)
                    ) { onDismiss ->
                        ResetWarningsBottomSheet { _, _ -> //isQuantitiesWarningEnabled, isTypesWarningEnabled ->
                            onDismiss()
                            // TODO: Save and apply
                        }
                    }
                }
            }
        }

        val onDismissInfoDialog = {
            showInfoDialog = false
        }

        if (showInfoDialog) {
            InfoDialog(getAppVersion(context), onDismissInfoDialog)
        }
    }
}

fun getAppVersion(
    context: Context,
): String? {
    return try {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        packageInfo.versionName
    } catch (e: Exception) {
        null
    }
}