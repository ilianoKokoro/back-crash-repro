package ca.ilianokokoro.backcrashrepro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ca.ilianokokoro.backcrashrepro.ui.theme.BackCrashReproTheme
import kotlinx.serialization.Serializable

@Serializable
data object FirstScreen : NavKey

@Serializable
data object SecondScreen : NavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BackCrashReproTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        NavigationRoot()
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(FirstScreen)

    NavDisplay(
        modifier = Modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = backStack::removeLastOrNull,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
        ),
        entryProvider = { key ->
            when (key) {

                is FirstScreen -> NavEntry(key) {
                    Column {
                        TextButton(onClick = { backStack.add(SecondScreen) }) {
                            Text("Go to screen 2")
                        }
                    }
                }

                is SecondScreen -> NavEntry(key) {
                    Column {
                        TextButton(onClick =  backStack::removeLastOrNull ) {
                            Text("Back")
                        }
                    CrashDropdown()
                    }
                }


                else -> throw RuntimeException()
            }
        })
}


@Composable
fun CrashDropdown() {
    var expanded by remember { mutableStateOf(false) }
    Column {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(24.dp),
        ) {
            Text("Crash contents")
        }
    }
}