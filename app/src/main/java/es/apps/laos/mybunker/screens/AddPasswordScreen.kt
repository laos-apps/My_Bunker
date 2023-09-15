package es.apps.laos.mybunker.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.apps.laos.mybunker.Field
import es.apps.laos.mybunker.FormState
import es.apps.laos.mybunker.PasswordEntity
import es.apps.laos.mybunker.R
import es.apps.laos.mybunker.Required
import es.apps.laos.mybunker.getDbConnection
import es.apps.laos.mybunker.ui.theme.MyBunkerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordScreen(navController: NavController) {
    MyBunkerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val state by remember { mutableStateOf(FormState()) }
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            // Icon button for going back
                            IconButton(onClick = {
                                navController.navigate(Screens.Home.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.go_home)
                                )
                            }
                        },
                        title = { Text(text = stringResource(R.string.new_password)) },
                    )
                },
                content = {
                    // In order to avoid that content is shown behind the top bar we have to pass PaddingValues
                    NewPasswordForm(state = state, paddingValues = it, navController = navController)
                }
            )
        }
    }
}

@Composable
fun NewPasswordForm(state: FormState, paddingValues: PaddingValues, navController: NavController) {
    val context: Context = LocalContext.current
    Box(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
    ) {
        Column {
            Form(
                state = state,
                fields = listOf(
                    Field(
                        name = TITLE_FIELD_NAME,
                        label = stringResource(R.string.title_web),
                        validators = listOf(Required(message = stringResource(R.string.title_web_is_required)))
                    ),
                    Field(
                        name = USER_FIELD_NAME,
                        label = stringResource(R.string.user),
                        validators = listOf(Required(message = stringResource(R.string.user_is_required)))
                    ),
                    Field(
                        name = PASSWORD_FIELD_NAME,
                        label = stringResource(R.string.password),
                        isPassword = true,
                        validators = listOf(Required(message = stringResource(R.string.password_is_required)))
                    ),
                    Field(
                        name = EXTRA_INFO_FIELD_NAME,
                        label = stringResource(R.string.extra_info),
                        validators = listOf(Required(message = stringResource(R.string.extra_info_is_required)))
                    )
                )
            )
            Row(
                modifier = Modifier.fillMaxSize().padding(end = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { //Actions for storing passwords
                        if (state.validate()) {
                            Log.i(
                                "MBK::AddPasswordScreen::NewPasswordForm",
                                "New form entry has been validated"
                            )
                            //Get values from fields in the form
                            val newPasswordEntryMap: Map<String, String> = state.getData()
                            // Logs the content of the form when save icon is pressed
                            newPasswordEntryMap.forEach { field ->
                                Log.d(
                                    "MBK::AddPasswordScreen::NewPasswordForm",
                                    "Field data: ${field.key}: ${field.value}"
                                )
                            }
                            //Store values in database
                            // Update existing password
                            getDbConnection(context = context).insertPassword(
                                passwordEntity = PasswordEntity(
                                    title = newPasswordEntryMap[TITLE_FIELD_NAME],
                                    user = newPasswordEntryMap[USER_FIELD_NAME],
                                    password = newPasswordEntryMap[PASSWORD_FIELD_NAME],
                                    extraInfo = newPasswordEntryMap[EXTRA_INFO_FIELD_NAME],
                                )
                            )
                            Log.d(
                                "MBK::AddPasswordScreen::NewPasswordForm",
                                "Updating password"
                            )

                            // Go back home
                            navController.navigate(Screens.Home.route)
                        }
                    },
                )
                { // Image for the icon will be a disk
                    Icon(Icons.Filled.Save, stringResource(R.string.save_password))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(R.string.save))
                }
            }
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