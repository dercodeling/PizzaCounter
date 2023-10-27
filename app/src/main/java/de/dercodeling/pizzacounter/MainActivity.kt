package de.dercodeling.pizzacounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.simulateHotReload
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dercodeling.pizzacounter.ui.theme.PizzaCounterTheme

var testPizzas = mapOf("Margherita" to 0, "Salami" to 0, "Prosciutto" to 0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzaCounterTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                ){
                    PizzaList(testPizzas)
                }
            }
        }
    }
}

@Composable
fun PizzaList(pizzas: Map<String,Int>){
    var types = mutableListOf<String>()
    pizzas.forEach{entry -> types.add(entry.key)}

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
    ){
        items(pizzas.size){ i ->
            var name = types[i]
            PizzaListItem(name = name)
        }
    }
}

@Composable
fun PizzaListItem(name: String){
    var number = 0
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ){
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = CenterVertically,
            modifier = Modifier.width(200.dp)
        ) {
            Text(
                text = number.toString(),
                color = Color.Red,
                fontSize = 30.sp
            )
            Text(
                text = "×",
                color = Color.Gray,
                fontSize = 20.sp
            )
            Text(
                text = name,
                color = Color.Green,
                fontSize = 30.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                )
        }
        Button(modifier = Modifier.padding(5.dp),onClick = {
            number+=1
            // TODO: Reload to show updated number
            // TODO: Check for overflow
        }) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp,contentDescription = null)
        }
        Button(onClick = {
            number-=1
            // TODO: Reload to show updated number
            // TODO: Check for overflow
        }) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown,contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PizzaListPreview() {
    PizzaCounterTheme {
        PizzaList(testPizzas)
    }
}