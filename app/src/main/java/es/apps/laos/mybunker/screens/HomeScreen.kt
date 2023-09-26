package es.apps.laos.mybunker.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.apps.laos.mybunker.MainActivityState
import es.apps.laos.mybunker.PasswordEntity
import es.apps.laos.mybunker.R
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
                                        contentDescription = stringResource(R.string.go_home)
                                    )
                                }
                            }
                        },
                        title = {
                            if (mainActivityState == MainActivityState.DEFAULT)
                                Text(text = stringResource(R.string.my_bunker))
                            else if (mainActivityState == MainActivityState.DELETE_PASSWORD)
                                Text(text = stringResource(R.string.delete_password))
                        },
                        actions = {
                            if (mainActivityState == MainActivityState.DEFAULT) {
                                IconButton(onClick = { displayMenuState = !displayMenuState }) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = stringResource(R.string.view_more)
                                    )
                                }
                                // Creating a dropdown menu
                                DropdownMenu(
                                    expanded = displayMenuState,
                                    onDismissRequest = { displayMenuState = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.export)) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.Output,
                                                contentDescription = stringResource(R.string.export)
                                            )
                                        },
                                        onClick = {
                                            Log.d(
                                                "MBK::HomeScreen::HomeScreen::DropdownMenuItem1",
                                                "Export option clicked"
                                            )
                                            navController.navigate(Screens.PdfExport.route)
                                        }
                                    )
                                    // Language option will be commented because language is auto-detected by android. Just translate strings.xml
                                    /*DropdownMenuItem(
                                        text = { Text(stringResource(R.string.language)) },
                                        leadingIcon = { Icon(imageVector = Icons.Filled.Language, contentDescription = stringResource(R.string.language)) },
                                        onClick = {
                                            Log.d(
                                            "MBK::HomeScreen::DropdownMenuItem2",
                                            "DropdownMenu option 2 clicked")
                                            // Go back to language settings screen
                                            navController.navigate(Screens.LanguageSettings.route)
                                        }
                                    )*/
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
                                        contentDescription = stringResource(R.string.delete_passwords)
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    // FAB is just shown when activity is in default state
                    if (mainActivityState == MainActivityState.DEFAULT) {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                            // Open new form for adding a new password
                            navController.navigate(Screens.AddPassword.route)
                        }) {
                            Icon(Icons.Filled.Add, stringResource(R.string.new_password))
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.End,
                content = { paddingValues ->
                    val passwordEntityList = getPasswordList(context)
                    if (passwordEntityList.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues = paddingValues),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // If there are no passwords, a message will be shown
                            Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.add_your_first_password)
                            )
                        }
                    } else {
                        // In order to avoid that content is shown behind the top-bar we have to pass padding values
                        Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                            PasswordList(
                                navController = navController,
                                passwordEntityList = passwordEntityList,
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
    // Colors
    val defaultCardBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer
    val defaultCardTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
    val selectedCardBackgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer
    val selectedCardTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = passwordEntityList) {
            var cardColor: Color by remember { mutableStateOf(defaultCardBackgroundColor) }
            var textColor: Color by remember { mutableStateOf(defaultCardTextColor) }
            ElevatedCard(
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
                            navController.navigate(Screens.EditPassword.route + "/${it.id}")
                        },
                        onLongClick = {
                            Log.v(
                                "MBK::HomeScreen::PasswordList::onLongClick",
                                "Card of password with id  ${it.id} was LONG clicked"
                            )

                            // Add/remove item to/from selected list
                            if (selectedPasswordArrayList.contains(it.id)) {
                                Log.v(
                                    "MBK::HomeScreen::PasswordList::onLongClick",
                                    "selectedPasswordArrayList contains id ${it.id}"
                                )
                                selectedPasswordArrayList.remove(it.id)
                                cardColor = defaultCardBackgroundColor
                                textColor = defaultCardTextColor
                            } else {
                                Log.v(
                                    "MBK::HomeScreen::PasswordList::onLongClick",
                                    "selectedPasswordArrayList DOES NOT contain id ${it.id}"
                                )
                                selectedPasswordArrayList.add(it.id)
                                cardColor = selectedCardBackgroundColor
                                textColor = selectedCardTextColor
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
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor,
                )
            ) {
                val startTextPadding = 10
                val bottomTextPadding = 10
                val endTextPadding = 10
                val topTextPadding = 10

                Text(
                    modifier = Modifier.padding(
                        start = startTextPadding.dp,
                        end = endTextPadding.dp,
                        top = topTextPadding.dp
                    ),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.title_web) + ": ")
                        }
                        append(it.title)
                    },
                    color = textColor
                )
                Text(
                    modifier = Modifier.padding(
                        start = startTextPadding.dp,
                        end = endTextPadding.dp
                    ),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.user) + ": ")
                        }
                        append(it.user)
                    },
                    color = textColor
                )
                if (it.extraInfo?.isEmpty() == true) {
                    Text(
                        modifier = Modifier.padding(
                            start = startTextPadding.dp,
                            end = endTextPadding.dp,
                            bottom = bottomTextPadding.dp // we add bottom padding in case there is no extra info
                        ),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.password) + ": ")
                            }
                            append("******")
                        },
                        color = textColor
                    )
                } else {// Extra info can be empty
                    Text(
                        modifier = Modifier.padding(
                            start = startTextPadding.dp,
                            end = endTextPadding.dp
                        ), text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.password) + ": ")
                            }
                            append("******")
                        },
                        color = textColor
                    )
                    Text(
                        modifier = Modifier.padding(
                            start = startTextPadding.dp,
                            end = endTextPadding.dp,
                            bottom = bottomTextPadding.dp
                        ), text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.extra_info) + ": ")
                            }
                            append(it.extraInfo)
                        },
                        color = textColor
                    )
                }
            }

        }
    }
}