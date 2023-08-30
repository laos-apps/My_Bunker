package es.apps.laos.mybunker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    NewPasswordScheme()
                }

            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewPasswordScheme() {
        val state by remember { mutableStateOf(FormState()) }
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
            floatingActionButton = {FloatingActionButton(
                onClick = { //TODO
                    if (state.validate()) {
                        Log.i(
                            "MBK::NewPasswordActivity::NewPasswordForm",
                            "This form works"
                        )
                    }
                },
            )
            { // Image for the icon will be a disk
                Icon(Icons.Filled.Save, "Save password")
            }},
            floatingActionButtonPosition = FabPosition.End,
            content = {
                // In order to avoid that content is shown behind the topbar we have to pass PaddingValues
                NewPasswordForm(state = state, paddingValues = it)
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewPasswordForm(state: FormState, paddingValues: PaddingValues) {
        Box(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
        ) {
            Column() {
                Form(
                    state = state,
                    fields = listOf(
                        Field(
                            name = "title",
                            label = "Title/Web",
                            validators = listOf(Required(message = "Title/Web is required"))
                        ),
                        Field(
                            name = "user",
                            label = "User",
                            validators = listOf(Required(message = "User is required"))
                        ),
                        Field(
                            name = "password",
                            label = "Password",
                            isPassword = true,
                            validators = listOf(Required(message = "Password is required"))
                        ),
                        Field(
                            name = "extraInfo",
                            label = "Extra info",
                            validators = listOf(Required(message = "Extra info is required"))
                        )
                    )
                )
            }
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