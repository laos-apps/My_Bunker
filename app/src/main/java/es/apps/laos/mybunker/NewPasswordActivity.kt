package es.apps.laos.mybunker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import es.apps.laos.mybunker.ui.theme.MyBunkerTheme

class NewPasswordActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                MyBunkerTheme() {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Generate sample data

                        NewPasswordTopAppBar()
                    }

                }
            }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewPasswordTopAppBar(){
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {finish()}) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Ir hacia arriba"
                            )
                        }
                    },
                    title = { Text(text = "New password") },
                )
            },
            content = {
                // In order to avoid that content is shown behind the topbar we have to pass paddingvAL
                Column(modifier = Modifier.padding(paddingValues = it)) {
                    // Content of the activity
                    NewPasswordForm()
                }
            }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun NewPasswordForm(){
        Column() {
            UserField()
            PasswordField()
            MailField()
            ExtraInfoField()
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserField(){
        var userText by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = userText,
            onValueChange = { userText = it },
            label = { Text("User") }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordField(){
        var passwordText by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text("Password") }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MailField(){
        var mailText by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = mailText,
            onValueChange = { mailText = it },
            label = { Text("Mail") }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ExtraInfoField(){
        var extraInfoText by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = extraInfoText,
            onValueChange = { extraInfoText = it },
            label = { Text("Extra info") }
        )
    }
}