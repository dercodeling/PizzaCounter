package de.dercodeling.pizzacounter.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R
import de.dercodeling.pizzacounter.domain.model.BottomSheetOption
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.ResetOption
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.ui.screens.components.OptionsBottomSheet
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListEvent
import de.dercodeling.pizzacounter.ui.screens.main.viewmodel.PizzaListState
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    state: PizzaListState,
    onEvent: (PizzaListEvent) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddTypeBottomSheet by remember { mutableStateOf(false) }
    var showSortBottomSheet by remember { mutableStateOf(false) }
    var showResetBottomSheet by remember { mutableStateOf(false) }
    var showResetQuantitiesDialog by remember { mutableStateOf(false) }
    var showResetTypesDialog by remember { mutableStateOf(false) }

    if (state.pizzaTypes.isEmpty()) {
        onEvent(PizzaListEvent.LoadInitialPizzaTypes)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
                //colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                scrollBehavior = scrollBehavior
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
                        onClick = { onEvent(PizzaListEvent.ShareList(shareStringPrefix, context)) }
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
                }
            )
        },
        snackbarHost = {
            SnackbarHost (snackbarHostState)
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val snackbarActionLabel = stringResource(R.string.snackbar_delete_button)
        val snackbarMessageNamePrefix = stringResource(R.string.snackbar_delete_message_name_prefix)
        val snackbarMessageNameSuffix = stringResource(R.string.snackbar_delete_message_name_suffix)

        PizzaList(
            state,
            onEvent = { event ->
                if (event is PizzaListEvent.DeletePizzaType && event.pizzaType.quantity > 0) {
                    val pizzaType = event.pizzaType

                    snackbarScope.launch {
                        val result = snackbarHostState
                            .showSnackbar(
                                message = snackbarMessageNamePrefix + pizzaType.name + snackbarMessageNameSuffix,
                                actionLabel = snackbarActionLabel,
                                duration = SnackbarDuration.Long
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                onEvent(PizzaListEvent.AddPizzaType(pizzaType))
                            }
                            else -> {}
                        }
                    }
                }

                onEvent(event)
            },
            Modifier.padding(innerPadding))

        if (showAddTypeBottomSheet) {
            val onDismiss: (PizzaType?) -> Unit = { newPizzaType ->

                if (newPizzaType != null) {
                    onEvent(PizzaListEvent.AddPizzaType(newPizzaType))
                }

                showAddTypeBottomSheet = false
            }
            AddTypeBottomSheet(state, onDismiss)
        }

        if (showSortBottomSheet) {
            val onDismiss: (BottomSheetOption?) -> Unit = { bottomSheetOption ->
                showSortBottomSheet = false

                if (bottomSheetOption is SortType) {
                    onEvent(PizzaListEvent.SetSortType(bottomSheetOption))
                }
            }

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
                    selected = (state.sortType == option.first),
                    onClick = {
                        closeAndApply(option.first)
                    })
            }
        }

        if (showResetBottomSheet) {
            val onDismiss: (BottomSheetOption?) -> Unit = {
                when (it) {
                    ResetOption.RESET_QUANTITIES -> showResetQuantitiesDialog = true
                    ResetOption.RESET_TYPES -> showResetTypesDialog = true
                    else -> {}
                }

                showResetBottomSheet = false
            }

            OptionsBottomSheet(
                onDismiss = onDismiss,
                heading = "",
                optionsMap = mapOf(
                    Pair(
                        ResetOption.RESET_QUANTITIES,
                        stringResource(R.string.reset_option_quantities)
                    ),
                    Pair(
                        ResetOption.RESET_TYPES,
                        stringResource(R.string.reset_option_types)
                    )
                )
            )
        }

        val onDismissResetQuantitiesDialog = {
            showResetQuantitiesDialog = false
        }

        if (showResetQuantitiesDialog) {
            AlertDialog(
                title = {
                    Text(text = stringResource(R.string.reset_quantities_dialog_header))
                },
                text = {
                    Text(text = stringResource(R.string.reset_quantities_dialog_text))
                },
                onDismissRequest = {
                    onDismissResetQuantitiesDialog()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(PizzaListEvent.ResetQuantities)
                            onDismissResetQuantitiesDialog()
                        }
                    ) {
                        Text(stringResource(R.string.reset_option_quantities))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissResetQuantitiesDialog()
                        }
                    ) {
                        Text(stringResource(R.string.dialog_cancel))
                    }
                }
            )
        }

        val onDismissResetTypesDialog = {
            showResetTypesDialog = false
        }

        if (showResetTypesDialog) {
            AlertDialog(
                title = {
                    Text(text = stringResource(R.string.reset_types_dialog_header))
                },
                text = {
                    Text(text = stringResource(R.string.reset_types_dialog_text))
                },
                onDismissRequest = {
                    onDismissResetTypesDialog()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(PizzaListEvent.ResetTypes)
                            onDismissResetTypesDialog()
                        }
                    ) {
                        Text(stringResource(R.string.reset_option_types))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissResetTypesDialog()
                        }
                    ) {
                        Text(stringResource(R.string.dialog_cancel))
                    }
                }
            )
        }
    }
}