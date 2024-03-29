package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.domain.model.ThemeOption
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterEvent
import de.dercodeling.pizzacounter.ui.main.viewmodel.PizzaCounterState
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    state: PizzaCounterState,
    onEvent: (PizzaCounterEvent) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val isLargeScreenLayout = state.windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Expanded
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddTypeBottomSheet by remember { mutableStateOf(false) }
    var showSortBottomSheet by remember { mutableStateOf(false) }
    var showResetBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🍕 " + stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToSettings,
                    ) {
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = stringResource(R.string.button_settings),

                        )
                    }
                },
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = { showSortBottomSheet = true },
                        modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.Sort,
                            contentDescription = stringResource(R.string.button_sort)
                        )
                    }
                    IconButton(onClick = { showResetBottomSheet = true }) {
                        Icon(
                            Icons.Rounded.RestartAlt,
                            contentDescription = stringResource(R.string.button_reset)
                        )
                    }

                    val shareStringPrefix = stringResource(R.string.share_string_prefix)
                    val context = LocalContext.current
                    IconButton(
                        onClick = { onEvent(PizzaCounterEvent.ShareList(shareStringPrefix, context)) }
                    ) {
                        Icon(
                            Icons.Rounded.Share,
                            contentDescription = stringResource(R.string.button_share)
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = {
                            showAddTypeBottomSheet = true
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.button_add)
                        )
                    }
                },
                contentPadding = if(isLargeScreenLayout) PaddingValues(20.dp, 0.dp) else PaddingValues()
            )
        },
        snackbarHost = {
            SnackbarHost (snackbarHostState)
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val snackbarActionLabel = stringResource(R.string.snackbar_delete_button)
        val snackbarMessageNamePrefix = stringResource(R.string.snackbar_delete_message_name_prefix)
        val snackbarMessageNameSuffix = stringResource(R.string.snackbar_delete_message_name_suffix)

        val pizzaListOnEvent: (PizzaCounterEvent) -> Unit = { event ->
            if (event is PizzaCounterEvent.DeletePizzaType && event.showSnackbar && event.pizzaType.quantity > 0) {
                val pizzaType = event.pizzaType

                snackbarScope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = snackbarMessageNamePrefix + pizzaType.name + snackbarMessageNameSuffix,
                            actionLabel = snackbarActionLabel,
                            duration = SnackbarDuration.Long
                        )
                    if (result == SnackbarResult.ActionPerformed) {
                        onEvent(PizzaCounterEvent.AddPizzaType(pizzaType))
                    }
                }
            }

            onEvent(event)
        }

        var pizzaListModifier = Modifier.padding(innerPadding)
        if (isLargeScreenLayout) {
            pizzaListModifier = pizzaListModifier
                .safeContentPadding()
                .padding(30.dp)
        }

        PizzaList(
            state.pizzaTypes,
            onEvent = pizzaListOnEvent,
            pizzaListModifier,
            prefixItem = {
                val newestVersionInfo = state.newestVersionInfo
                if(newestVersionInfo.isNewestVersion == false)
                    NewVersionBanner(
                        releaseUrl = newestVersionInfo.newestReleaseUrl,
                        versionNumber = newestVersionInfo.newestVersionNumber
                    )
            }
        )

        if (showAddTypeBottomSheet) {
            val onDismiss: (PizzaType?) -> Unit = { newPizzaType ->

                if (newPizzaType != null) {
                    onEvent(PizzaCounterEvent.AddPizzaType(newPizzaType))
                }

                showAddTypeBottomSheet = false
            }
            AddTypeBottomSheet(
                currentPizzaTypes = state.pizzaTypes,
                onDismiss)
        }

        SortBottomSheet(
            visible = showSortBottomSheet,
            currentSortType = state.sortType,
            onDismiss = { bottomSheetOption ->
                if (bottomSheetOption is SortType) {
                    onEvent(PizzaCounterEvent.SetSortType(bottomSheetOption))
                }

                showSortBottomSheet = false
            }
        )

        ResetBottomSheet(
            visible = showResetBottomSheet,
            onDismiss = {
                showResetBottomSheet = false
            },
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
fun NewVersionBanner(
    versionNumber: String?,
    releaseUrl: String?
) {
    val uriHandler = LocalUriHandler.current

    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if(!releaseUrl.isNullOrEmpty()) uriHandler.openUri(releaseUrl)
            }
            .padding(0.dp, 0.dp, 0.dp, 30.dp)
    ) {
        Column (
            Modifier.padding(15.dp)
        ) {
            Text(
                stringResource(R.string.new_version_banner_heading),
                style = MaterialTheme.typography.headlineSmall
            )
            val safeVersionNumber = versionNumber ?: ""
            Text(stringResource(R.string.new_version_banner_content,safeVersionNumber))
        }
    }
}

//@PreviewLightDark
@PreviewDynamicColors
//@PreviewFontScale
//@PreviewScreenSizes
@Composable
fun MainScreenPreview() {
    PizzaCounterTheme (
        themeSetting = ThemeOption.SYSTEM
    ) {
        MainScreen(PizzaCounterState(
            pizzaTypes = listOf(
                PizzaType("Arrabbiata",1),
                PizzaType("Margherita",4),
                PizzaType("Salami",0),
                @Suppress("SpellCheckingInspection")
                PizzaType("Tonno",3,"con cipolla rossa"),
            )
        ), {}, {})
    }
}