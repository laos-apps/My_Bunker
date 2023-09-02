package es.apps.laos.mybunker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        setContent{
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
        val passwordEntityList: ArrayList<PasswordEntity> = passwordDao.getAll() as ArrayList<PasswordEntity>
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
    Scaffold(
        topBar = {
            TopAppBar(
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
            // In order to avoid that content is shown behind the topbar we have to pass paddingvalues
            Column(modifier = Modifier.padding(paddingValues = it)) {
                PasswordList(passwordEntityList = passwordEntityList)
            }
        }
    )
}

// Lazy column list for showing a preview of every password
@Composable
fun PasswordList(passwordEntityList: ArrayList<PasswordEntity>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = passwordEntityList) { it ->
            PasswordEntryView(passwordEntity = it)
        }
    }
}

// Password preview for a password
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordEntryView(passwordEntity: PasswordEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = {
            Log.v(
                "MBK::MainActivity::PasswordEntryView",
                "Card of password with id  ${passwordEntity.id} was clicked"
            )
        }
    ) {
        Text(text = "Title: ${passwordEntity.title}")
        Text(text = "User: ${passwordEntity.user}")
        Text(text = "Password: ******")
        Text(text = "Extra info: ${passwordEntity.extraInfo}")
    }
}