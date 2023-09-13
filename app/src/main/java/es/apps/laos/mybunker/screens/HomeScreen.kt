package es.apps.laos.mybunker.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.apps.laos.mybunker.AppDatabase
import es.apps.laos.mybunker.MainActivityState
import es.apps.laos.mybunker.PasswordDao
import es.apps.laos.mybunker.PasswordEntity
import es.apps.laos.mybunker.getDbConnection
import es.apps.laos.mybunker.ui.theme.MyBunkerTheme

// NON-COMPOSABLE METHODS
fun getPasswordList(context: Context): ArrayList<PasswordEntity> {
    // Query data in DB and show stored passwords
    // Get all password entries
    val passwordEntityList: ArrayList<PasswordEntity> =
        getDbConnection(context = context).getAll() as ArrayList<PasswordEntity>
    Log.d(
        "MBK::MainActivity::getPasswordList()",
        "Number of stored passwords: ${passwordEntityList.size}"
    )
    return passwordEntityList
}




// COMPOSABLE METHODS
// Compose main view for listing passwords
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // Gets context for composable methods
    val context: Context = LocalContext.current
    // Stores the current state of the main activity
    var mainActivityState by remember { mutableStateOf(MainActivityState.DEFAULT) }
    // Stores the display menu state
    var displayMenuState by remember { mutableStateOf(false) }
    // Stores an integer list with passwords to be deleted when selected
    var passwordListToDelete: ArrayList<Int> = ArrayList()

    // Depending on mainActivityState, the UI will change
    MyBunkerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            // Show back arrow only in delete mode
                            if (mainActivityState == MainActivityState.DELETE_PASSWORD) {
                                // Icon button for going back
                                IconButton(onClick = {
                                    navController.navigate(Screens.Home.route)
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Go home"
                                    )
                                }
                            }
                        },
                        title = {
                            if (mainActivityState == MainActivityState.DEFAULT)
                                Text(text = "My Bunker")
                            else if (mainActivityState == MainActivityState.DELETE_PASSWORD)
                                Text(text = "Delete password")
                        },
                        actions = {
                            if (mainActivityState == MainActivityState.DEFAULT) {
                                IconButton(onClick = { displayMenuState = !displayMenuState}) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "Ver más"
                                    )
                                }
                                // Creating a dropdown menu
                                DropdownMenu(
                                    expanded = displayMenuState,
                                    onDismissRequest = { displayMenuState = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Export") },
                                        leadingIcon = { Icon(imageVector = Icons.Filled.Output, contentDescription = "Export") },
                                        onClick = {Log.d(
                                            "MBK::HomeScreen::HomeScreen::DropdownMenuItem1",
                                            "DropdownMenu option 1 clicked"
                                        ) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Language") },
                                        leadingIcon = { Icon(imageVector = Icons.Filled.Language, contentDescription = "Language") },
                                        onClick = {
                                            Log.d(
                                            "MBK::HomeScreen::DropdownMenuItem2",
                                            "DropdownMenu option 2 clicked")
                                            // Go back to language settings screen
                                            navController.navigate(Screens.LanguageSettings.route)
                                        }
                                    )
                                }
                            } else if (mainActivityState == MainActivityState.DELETE_PASSWORD) {
                                IconButton(onClick = {
                                    passwordListToDelete.forEach { passwordID: Int ->
                                        // Delete password. First, we get password object by id
                                        getDbConnection(context = context).delete(
                                            getDbConnection(
                                                context = context
                                            ).getPasswordById(passwordID)
                                        )
                                        Log.d(
                                            "MBK::HomeScreen::HomeScreen",
                                            "Password deleted: $passwordID"
                                        )
                                        // Go back to home screen
                                        navController.navigate(Screens.Home.route)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete passwords"
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    // FAB is just shown when activity is in default state
                    if (mainActivityState == MainActivityState.DEFAULT) {
                        FloatingActionButton(onClick = {
                            // Open new form for adding a new password
                            navController.navigate(Screens.AddPassword.route)
                        }) {
                            Icon(Icons.Filled.Add, "New password")
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.End,
                content = { paddingValues ->
                    // In order to avoid that content is shown behind the top-bar we have to pass padding values
                    Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                        PasswordList(
                            navController = navController,
                            passwordEntityList = getPasswordList(context),
                            changeState = { _mainActivityState ->
                                mainActivityState = _mainActivityState
                            },
                            passwordListToDelete = { _passwordListToDelete ->
                                passwordListToDelete = _passwordListToDelete
                                Log.v(
                                    "MBK::HomeScreen::HomeScreen::content}",
                                    "Content of passwordListToDelete: $passwordListToDelete"
                                )
                            }
                        )
                    }
                }
            )
        }
    }
}

// Lazy column list for showing a preview of every password
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PasswordList(
    navController: NavController,
    passwordEntityList: List<PasswordEntity>,
    changeState: (MainActivityState) -> Unit,
    passwordListToDelete: (ArrayList<Int>) -> Unit
) {
    // Using State<List<T>> and immutable listOf()
    val selectedPasswordMutableStateList: MutableState<List<Int>> =
        remember { mutableStateOf(listOf()) } // Initialize
    // Update the list state with a new list
    val selectedPasswordArrayList: ArrayList<Int> = ArrayList()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = passwordEntityList) {
            var colorCard: Color by remember { mutableStateOf(Color.Cyan) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .combinedClickable(
                        onClick = {
                            Log.v(
                                "MBK::HomeScreen::PasswordList",
                                "Card of password with id  ${it.id} was clicked"
                            )
                            // Open new form for adding a new password, we pass password id
                            Log.v(
                                "MBK::HomeScreen::PasswordList",
                                "Route passed to navController: ${Screens.EditPassword.route}/${it.id}"
                            )
                            navController.navigate(Screens.EditPassword.route+"/${it.id}")
                        },
                        onLongClick = {
                            Log.v(
                                "MBK::HomeScreen::PasswordList::onLongClick",
                                "Card of password with id  ${it.id} was LONG clicked"
                            )

                            // Add/remove item to/from selected list
                            colorCard = if (selectedPasswordArrayList.contains(it.id)) {
                                Log.v(
                                    "MBK::HomeScreen::PasswordList::onLongClick",
                                    "selectedPasswordArrayList contains id ${it.id}"
                                )
                                selectedPasswordArrayList.remove(it.id)
                                Color.Cyan
                            } else {
                                Log.v(
                                    "MBK::HomeScreen::PasswordList::onLongClick",
                                    "selectedPasswordArrayList DOES NOT contain id ${it.id}"
                                )
                                selectedPasswordArrayList.add(it.id)
                                Color.Red
                            }
                            Log.d(
                                "MBK::HomeScreen::PasswordList::onLongClick",
                                "selectedPasswordArrayList content: $selectedPasswordArrayList"
                            )
                            // Update
                            selectedPasswordMutableStateList.value =
                                selectedPasswordArrayList.toList()
                            // If some password is selected, enter to delete state
                            if (selectedPasswordArrayList.isNotEmpty()) {
                                changeState(MainActivityState.DELETE_PASSWORD)
                                passwordListToDelete(selectedPasswordArrayList)
                            } else
                                changeState(MainActivityState.DEFAULT)
                        }
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorCard,
                )
            ) {
                Text(text = "Title: ${it.title}")
                Text(text = "User: ${it.user}")
                Text(text = "Password: ******")
                Text(text = "Extra info: ${it.extraInfo}")
            }
        }
    }
}