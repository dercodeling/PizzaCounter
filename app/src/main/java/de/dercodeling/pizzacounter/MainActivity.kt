package de.dercodeling.pizzacounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PizzaListDatabase::class.java,
            "pizza_list.db"
        ).build()
    }

    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao) as T
                }
            }
        }
    )

    private fun init(viewModel: MainViewModel) {
        viewModel.clearTypes()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            // Create PizzaCounterTheme with or without darkMode-Boolean depending whether system theming is followed

            val themeSetting = viewModel.getTheme()

            if (themeSetting.isFollowSystem) {
                PizzaCounterTheme {
                    PizzaCounterActivity()
                }
            } else {
                PizzaCounterTheme(darkTheme = themeSetting.isDark) {
                    PizzaCounterActivity()
                }
            }

        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun PizzaCounterActivity() {
        var showAddTypeBottomSheet by remember { mutableStateOf(false) }
        var showSortBottomSheet by remember { mutableStateOf(false) }
        var showClearBottomSheet by remember { mutableStateOf(false) }
        var showClearQuantitiesDialog by remember { mutableStateOf(false) }
        var showClearTypesDialog by remember { mutableStateOf(false) }

        if (viewModel.getSize() == 0) {
            init(viewModel)
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "🍕 " + getString(R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            //TODO: Create settings page
                        }) {
                            /*Icon(
                                Icons.Rounded.Settings,
                                contentDescription = getString(R.string.button_settings)
                            )*/
                        }
                    }

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
                                contentDescription = getString(R.string.button_sort)
                            )
                        }
                        IconButton(onClick = { showClearBottomSheet = true }) {
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = getString(R.string.button_clear)
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            onClick = {
                                showAddTypeBottomSheet = true
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = getString(R.string.button_add)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->

            PizzaList(viewModel, innerPadding)

            if (showAddTypeBottomSheet) {
                val onDismiss: () -> Unit = { showAddTypeBottomSheet = false }
                AddTypeBottomSheet(onDismiss)
            }

            if (showSortBottomSheet) {
                val onDismiss: (SortType?) -> Unit = {
                    showSortBottomSheet = false

                    if (it != null) {
                        viewModel.setSortBy(it)
                    }
                }
                SortBottomSheet(onDismiss, viewModel.getSortBy())
            }

            if (showClearBottomSheet) {
                val onDismiss: (Int) -> Unit = {
                    when (it) {
                        0 -> showClearQuantitiesDialog = true
                        1 -> showClearTypesDialog = true
                    }

                    showClearBottomSheet = false
                }

                ClearBottomSheet(onDismiss)
            }

            val onDismissClearQuantitiesDialog = {
                showClearQuantitiesDialog = false
            }

            if (showClearQuantitiesDialog) {
                AlertDialog(
                    title = {
                        Text(text = getString(R.string.clear_quantities_dialog_header))
                    },
                    text = {
                        Text(text = getString(R.string.clear_quantities_dialog_text))
                    },
                    onDismissRequest = {
                        onDismissClearQuantitiesDialog()
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.clearQuantities()
                                onDismissClearQuantitiesDialog()
                            }
                        ) {
                            Text(getString(R.string.clearing_option_quantities))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onDismissClearQuantitiesDialog()
                            }
                        ) {
                            Text(getString(R.string.dialog_cancel))
                        }
                    }
                )
            }

            val onDismissClearTypesDialog = {
                showClearTypesDialog = false
            }

            if (showClearTypesDialog) {
                AlertDialog(
                    title = {
                        Text(text = getString(R.string.clear_types_dialog_header))
                    },
                    text = {
                        Text(text = getString(R.string.clear_types_dialog_text))
                    },
                    onDismissRequest = {
                        onDismissClearTypesDialog()
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.clearTypes()
                                onDismissClearTypesDialog()
                            }
                        ) {
                            Text(getString(R.string.clearing_option_types))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onDismissClearTypesDialog()
                            }
                        ) {
                            Text(getString(R.string.dialog_cancel))
                        }
                    }
                )
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun AddTypeBottomSheet(onDismiss: () -> Unit) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
            dragHandle = {}
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                var textFieldValue by remember { mutableStateOf("") }
                val textFieldFocusRequester = FocusRequester()
                //val focusManager = LocalFocusManager.current

                fun closeAndAddPizzaType() {
                    if (textFieldValue.isNotEmpty()) {
                        //focusManager.clearFocus()

                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss()
                            }
                        }

                        viewModel.addType(textFieldValue)
                    } else {
                        print("Empty type was not added.")
                        // TODO: Show snack-bar with empty-type-warning
                    }
                }

                TextField(
                    value = textFieldValue,
                    placeholder = { Text(getString(R.string.hint_new_type)) },

                    singleLine = true,
                    onValueChange = { textFieldValue = it },

                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(textFieldFocusRequester)
                        .padding(10.dp),

                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),

                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),

                    keyboardActions = KeyboardActions(
                        onDone = {
                            closeAndAddPizzaType()
                        }
                    )
                )

                LaunchedEffect(Unit) {
                    textFieldFocusRequester.requestFocus()
                }

                TextButton(onClick = { closeAndAddPizzaType() }) {
                    Text(getString(R.string.button_add))
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun SortBottomSheet(onDismiss: (sortBy: SortType?) -> Unit, currentSorting: SortType) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        fun closeAndSetSort(sortBy: SortType?) {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismiss(sortBy)
                }
            }
        }

        ModalBottomSheet(
            onDismissRequest = { closeAndSetSort(null) },
            sheetState = sheetState,
            dragHandle = {}
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(30.dp)
            ) {
                Column(verticalArrangement = Arrangement.SpaceEvenly) {


                    Text(getString(R.string.heading_sort)) // TODO: Stylize (e.g. like in Google Tasks)

                    val sortTypes = mapOf(
                        Pair(
                            SortType.NAME,
                            getString(R.string.sorting_option_name)
                        ),
                        Pair(
                            SortType.QUANTITY_ASC,
                            getString(R.string.sorting_option_quantity_ascending)
                        ),
                        Pair(
                            SortType.QUANTITY_DESC,
                            getString(R.string.sorting_option_quantity_descending)
                        )
                    )

                    for (sortType in sortTypes) {
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(40))
                                .clickable {
                                    closeAndSetSort(sortType.key)
                                }
                        ) {
                            RadioButton(
                                selected = (currentSorting == sortType.key),
                                onClick = {
                                    closeAndSetSort(sortType.key)
                                })
                            Text(sortType.value)
                        }
                    }
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun ClearBottomSheet(onDismiss: (Int) -> Unit) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        fun closeAndClear(clearingOption: Int) {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismiss(clearingOption)
                }
            }
        }

        ModalBottomSheet(
            onDismissRequest = { closeAndClear(-1) },
            sheetState = sheetState,
            dragHandle = {}
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(30.dp)
            ) {
                Column(verticalArrangement = Arrangement.SpaceEvenly) {

                    Text(getString(R.string.heading_clear)) // TODO: Stylize (e.g. like in Google Tasks)

                    val options = listOf(
                        getString(R.string.clearing_option_quantities),
                        getString(R.string.clearing_option_types)
                    )
                    val icons = listOf(Icons.Rounded.Clear, Icons.Rounded.RestartAlt)

                    for (option in options) {
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(40))
                                .clickable {
                                    closeAndClear(options.indexOf(option))
                                }
                                .padding(13.dp)
                        ) {
                            Icon(
                                icons[options.indexOf(option)],
                                contentDescription = option,
                                modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                            )
                            Text(option)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PizzaList(viewModel: MainViewModel, innerPadding: PaddingValues) {
        LazyColumn(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(0.dp, 30.dp, 0.dp, 0.dp)
        ) {
            val types = viewModel.getTypes()

            items(types.size) { i ->
                PizzaListItem(viewModel, type = types[i])
            }
            item {
                Spacer(Modifier.padding(15.dp))
            }
        }
    }

    @Composable
    fun PizzaListItem(viewModel: MainViewModel, type: String) {
        Card(
            Modifier.padding(15.dp, 10.dp)
        ) {
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp, 15.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text( // Quantity
                        text = viewModel.getQuantity(type).toString(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "×",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(15.dp)
                    )
                    Text( // Type
                        text = type,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Button(modifier = Modifier.padding(5.dp), onClick = {
                        viewModel.changeQuantity(type, 1)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = getString(R.string.button_increase)
                        )
                    }
                    Button(onClick = {
                        viewModel.changeQuantity(type, -1)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Remove,
                            contentDescription = getString(R.string.button_decrease)
                        )
                    }
                }
            }
        }
    }
}