package es.apps.laos.mybunker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import es.apps.laos.mybunker.ui.theme.MyBunkerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBunkerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainViewScheme(passwordEntityList = getPasswordList())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setContent {
            Log.v(
                "MBK::MainActivity::onResume",
                "Main Activity resumed"
            )
            MainViewScheme(passwordEntityList = getPasswordList())
        }
    }

    private fun getPasswordList(): ArrayList<PasswordEntity> {
        // Query data in DB and show stored passwords
        // Get DB connection
        var passwordDao: PasswordDao =
            AppDatabase.getInstance(this)?.passwordDao()!!
        // Get all password entries
        val passwordEntityList: ArrayList<PasswordEntity> =
            passwordDao.getAll() as ArrayList<PasswordEntity>
        Log.d(
            "MBK::MainActivity::onCreate",
            "Number of stored passwords: ${passwordEntityList.size}"
        )
        return passwordEntityList
    }
}

// Compose design for viewing passwords
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainViewScheme(passwordEntityList: ArrayList<PasswordEntity>) {
    val context: Context = LocalContext.current
    var mainActivityState by remember { mutableStateOf(MainActivityState.DEFAULT) }

    // Depending on mainActivityState, the UI will change
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (mainActivityState == MainActivityState.DEFAULT)
                        Text(text = "My Bunker")
                    else if (mainActivityState == MainActivityState.DELETE_PASSWORD)
                        Text(text = "Delete password")
                },
                actions = {
                    if (mainActivityState == MainActivityState.DEFAULT) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Share, contentDescription = "Compartir")
                        }

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Ver más"
                            )
                        }
                    } else if (mainActivityState == MainActivityState.DELETE_PASSWORD) {
                        IconButton(onClick = { /*TODO*/ }) {
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
                    val intent = Intent(context, NewPasswordActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.Add, "New password")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { paddingValues ->
            // In order to avoid that content is shown behind the topbar we have to pass paddingvalues
            Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                PasswordList(
                    passwordEntityList = passwordEntityList,
                    changeState = { _mainActivityState ->
                        mainActivityState = _mainActivityState
                    }
                )
            }
        }
    )
}

// Lazy column list for showing a preview of every password
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PasswordList(
    passwordEntityList: ArrayList<PasswordEntity>,
    changeState: (MainActivityState) -> Unit
) {
    // Using State<List<T>> and immutable listOf()
    val selectedPasswordMutableStateList: MutableState<List<Int>> =
        remember { mutableStateOf(listOf()) } // Initialize
    // Update the list state with a new list
    var selectedPasswordArrayList: ArrayList<Int> = ArrayList()

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
                                "MBK::MainActivity::PasswordEntryView",
                                "Card of password with id  ${it.id} was clicked"
                            )
                        },
                        onLongClick = {
                            Log.v(
                                "MBK::MainActivity::PasswordEntryView",
                                "Card of password with id  ${it.id} was LONG clicked"
                            )

                            // Add/remove item to/from selected list
                            if (selectedPasswordArrayList.contains(it.id)) {
                                Log.v(
                                    "MBK::MainActivity::PasswordList::onLongClick",
                                    "selectedPasswordArrayList contains id ${it.id}"
                                )
                                selectedPasswordArrayList.remove(it.id)
                                colorCard = Color.Cyan
                            } else {
                                Log.v(
                                    "MBK::MainActivity::PasswordList::onLongClick",
                                    "selectedPasswordArrayList DOES NOT contain id ${it.id}"
                                )
                                selectedPasswordArrayList.add(it.id)
                                colorCard = Color.Red
                            }
                            Log.d(
                                "MBK::MainActivity::PasswordList::onLongClick",
                                "selectedPasswordArrayList content: $selectedPasswordArrayList"
                            )
                            // Update
                            selectedPasswordMutableStateList.value =
                                selectedPasswordArrayList.toList()
                            // If some password is selected, enter to delete state
                            if (selectedPasswordArrayList.isNotEmpty())
                                changeState(MainActivityState.DELETE_PASSWORD)
                            else
                                changeState(MainActivityState.DEFAULT)
                        }
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorCard,
                )
            ) {
                Log.v(
                    "MBK::MainActivity::PasswordList",
                    "Content of selectedPasswordMutableStateList: ${selectedPasswordMutableStateList.value}"
                )
                Text(text = "Title: ${it.title}")
                Text(text = "User: ${it.user}")
                Text(text = "Password: ******")
                Text(text = "Extra info: ${it.extraInfo}")
            }
        }
    }
}