package de.dercodeling.pizzacounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Remove
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private fun init(viewModel: MainViewModel){
        viewModel.addType("Margherita")
        viewModel.addType("Prosciutto")
        viewModel.addType("Salami")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            PizzaCounterTheme {
                val viewModel by remember { mutableStateOf(MainViewModel()) }
                //var viewModel = MainViewModel()
                init(viewModel)

                var showAddTypeBottomSheet by remember { mutableStateOf(false) }
                var showSortBottomSheet by remember { mutableStateOf(false) }
                var showClearBottomSheet by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    "🍕 " + getString(R.string.app_name),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold)
                            },
                        )
                    },
                    bottomBar = {
                        BottomAppBar (

                            actions = {
                                IconButton(onClick = { showSortBottomSheet = true }) {
                                    Icon(Icons.AutoMirrored.Rounded.Sort, contentDescription = "Sort")
                                }
                                IconButton(onClick = { showClearBottomSheet = true }) {
                                    Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                                }
                            },
                            floatingActionButton = {
                                FloatingActionButton(
                                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                                    onClick = {
                                        showAddTypeBottomSheet = true
                                    }
                                ){Icon(Icons.Rounded.Add, contentDescription = "Add")}
                            }
                        )
                    }
                ) { innerPadding ->
                    var sortBy by remember{ mutableStateOf("Type") }

                    PizzaList(viewModel, sortBy, innerPadding)

                    if (showAddTypeBottomSheet) {
                        AddTypeBottomSheet({ showAddTypeBottomSheet = false }, viewModel)
                    }

                    if (showSortBottomSheet) {
                        val onDismiss : (String) -> Unit = {
                            showSortBottomSheet = false
                            sortBy = it
                        }
                        SortBottomSheet(onDismiss, sortBy)
                    }

                    /*if (showClearBottomSheet) { // TODO: Implement clearing: bottom sheet with option to clear the quantities or quantities and types
                        ClearBottomSheet({showClearBottomSheet = false}, viewModel)
                    }*/
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun AddTypeBottomSheet(onDismiss: () -> Unit, viewModel: MainViewModel){
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
            dragHandle = {}
        ) {
            Column (
                horizontalAlignment = Alignment.End
            ) {
                var textFieldValue by remember { mutableStateOf("") }
                val textFieldFocusRequester = FocusRequester()
                //val focusManager = LocalFocusManager.current

                fun closeAndAddPizzaType(){
                    if(textFieldValue.isNotEmpty()) {
                        //focusManager.clearFocus()

                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss()
                            }
                        }

                        viewModel.addType(textFieldValue)
                    }else{
                        print("Empty type was not added.")
                        // TODO: Show snack-bar with empty-type-warning
                    }
                }

                TextField(
                    value = textFieldValue,
                    placeholder = { Text("New type") },

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
                    Text("Add")
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun SortBottomSheet(onDismiss: (sortBy: String) -> Unit, currentSorting: String){
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        var sortBy = currentSorting
        println("currently: $sortBy")

        ModalBottomSheet(
            onDismissRequest = { onDismiss(sortBy) },
            sheetState = sheetState,
            dragHandle = {}
        ) {
            Column (
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(30.dp)
            ) {
                Column ( verticalArrangement = Arrangement.SpaceEvenly) {
                    fun closeAndSetSort(){
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss(sortBy)
                            }
                        }
                    }

                    Text("Sort by:")

                    val options = listOf<String>("Type","Quantity")

                    for(option in options){
                        Row (
                            verticalAlignment = CenterVertically
                        ){
                            val optionText = option

                            RadioButton(selected = (sortBy == optionText), onClick = {
                                sortBy = optionText
                                closeAndSetSort()
                            })
                            Text(optionText)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PizzaList(viewModel: MainViewModel, sortBy: String, innerPadding: PaddingValues){
        LazyColumn(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(0.dp, 30.dp, 0.dp, 0.dp)
        ) {
            items(viewModel.getSize()) { i ->
                viewModel.sortBy(sortBy)
                PizzaListItem(viewModel, type = viewModel.getType(i))
            }
        }
    }

    @Composable
    fun PizzaListItem(viewModel: MainViewModel, type: String){
        Card (
            Modifier.padding(15.dp,10.dp)
        ){
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp, 15.dp)
            ){
                Row (
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row (
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Button(modifier = Modifier.padding(5.dp),onClick = {
                        viewModel.changeQuantity(type, 1)
                    }) {
                        Icon(imageVector = Icons.Rounded.Add,contentDescription = "Increase")
                    }
                    Button(onClick = {
                        viewModel.changeQuantity(type, -1)
                    }) {
                        Icon(imageVector = Icons.Rounded.Remove,contentDescription = "Decrease")
                    }
                }
            }
        }
    }
}