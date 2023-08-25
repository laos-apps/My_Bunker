package es.apps.laos.mybunker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.materialIcon
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
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
                    // Generate sample data
                    val passwordEntryLists: ArrayList<PasswordEntry> = arrayListOf(
                        PasswordEntry(
                            user = "Alberto",
                            password = "mypass",
                            mail = null,
                            extraInfo = null
                        ),
                        PasswordEntry(
                            user = "Luis",
                            password = "33hddns",
                            mail = null,
                            extraInfo = null
                        ),
                        PasswordEntry(
                            user = "Pepe",
                            password = "$56´._",
                            mail = null,
                            extraInfo = null
                        ),
                    )
                    BunkerTopAppBar(passwordEntryList = passwordEntryLists)
                }
            }
        }
    }
}

// App top bar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BunkerTopAppBar(passwordEntryList: ArrayList<PasswordEntry>) {
    val context: Context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Ir hacia arriba"
                        )
                    }
                },
                title = { Text(text = "My Bunker") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Compartir")
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Ver más")
                    }
                }
            )
        },
        floatingActionButton = { FloatingActionButton(onClick = {
            // Open new form for adding a new password
            val intent = Intent(context,NewPasswordActivity::class.java)
            context.startActivity(intent)
        }){
            Icon(Icons.Filled.Add, "New password")
        } },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            // In order to avoid that content is shown behind the topbar we have to pass paddingvAL
            Column(modifier = Modifier.padding(paddingValues = it)) {
                PasswordList(passwordEntryList = passwordEntryList)
            }
        }
    )
}

// Lazy column list for showing a preview of every password
@Composable
fun PasswordList(passwordEntryList: ArrayList<PasswordEntry>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = passwordEntryList) { it ->
            PasswordEntryView(passwordEntry = it)
        }
    }
}

// Password preview for a password
@Composable
fun PasswordEntryView(passwordEntry: PasswordEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Text(text = "User: ${passwordEntry.user}")
        Text(text = "Password: ******")
        Text(text = "User: ${passwordEntry.mail}")
        Text(text = "Extra info: ${passwordEntry.extraInfo}")
    }
}