package es.apps.laos.mybunker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.snackbar.Snackbar
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
    fun NewPasswordTopAppBar() {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun NewPasswordForm() {
        val state by remember { mutableStateOf(FormState()) }

        Column() {
            Form(
                state = state,
                fields = listOf(
                    Field(name = "title", label = "Title/Web", validators = listOf(Required())),
                    Field(name = "user", label = "User", validators = listOf(Required())),
                    Field(name = "password", label = "Password", isPassword = true, validators = listOf(Required())),
                    Field(name = "extraInfo", label = "Extra info", validators = listOf(Required()))
                )
            )
        }

        FloatingActionButton(
            onClick = { //TODO
                if (state.validate()) Log.i(
                    "NewPasswordActivity::NewPasswordForm",
                    "This form works"
                )
            },
        )
        {
            Icon(Icons.Filled.Save, "Save password")
        }

    }

    @Composable
    fun Form(state: FormState, fields: List<Field>) {
        state.fields = fields

        Column {
            fields.forEach {
                it.Content()
            }
        }
    }


}