package de.dercodeling.pizzacounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme

var viewModel = MainViewModel()

class MainActivity : ComponentActivity() {
    fun init(){ // TODO: Work with remember keyword to hopefully preserve value across recreation (Tutorial 34:06)
        viewModel.addType("Margherita")
        viewModel.addType("Prosciutto")
        viewModel.addType("Salami")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        init()

        setContent {
            PizzaCounterTheme {
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
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                    // TODO: Add remaining scaffolding parts
                ) { innerPadding ->
                    LazyColumn(
                        Modifier.padding(innerPadding)
                            .fillMaxSize()
                            //.imePadding()
                            .padding(0.dp,30.dp,0.dp,0.dp)
                    ) {
                        items(viewModel.getSize()) { i ->
                            PizzaListItem(type = viewModel.getType(i))
                        }
                        /*item {
                            Spacer(
                                Modifier.windowInsetsBottomHeight(
                                    WindowInsets.systemBars
                                )
                            )
                        }*/
                    }
                }
            }
        }
    }
}

@Composable
fun PizzaListItem(type: String){
    Card (
        Modifier.padding(15.dp,10.dp)
    ){
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                //.background(MaterialTheme.colorScheme.primaryContainer)
                .padding(25.dp, 15.dp)
        ){
            Row (
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = CenterVertically,
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
                    Icon(imageVector = Icons.Default.KeyboardArrowUp,contentDescription = null)
                }
                Button(onClick = {
                    viewModel.changeQuantity(type, -1)
                }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown,contentDescription = null)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PizzaListPreview() {
    PizzaCounterTheme {
        LazyColumn(
            Modifier
                .padding(25.dp)
                .fillMaxSize()
            /*.padding(25.dp)*/
        ) {
            items(viewModel.getSize()){ i ->
                PizzaListItem(type = viewModel.getType(i))
            }
        }
    }
}