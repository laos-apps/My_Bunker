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
fun EditPasswordScreen(navController: NavController, passwordId: Int) {
    Log.i(
        "MBK::EditPasswordScreen::EditPasswordScreen",
        "Opening Edit Password Screen for password id: $passwordId"
    )
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
                        title = { Text(text = stringResource(R.string.edit_password)) },
                    )
                },
                content = {
                    // In order to avoid that content is shown behind the top bar we have to pass PaddingValues
                    EditPasswordForm(state = state, paddingValues = it, navController = navController, passwordId = passwordId)
                }
            )
        }
    }
}

@Composable
fun EditPasswordForm(state: FormState, paddingValues: PaddingValues, navController: NavController, passwordId: Int) {
    val context: Context = LocalContext.current
    // Get password entity from database
    val passwordEntity: PasswordEntity = getDbConnection(context = context).getPasswordById(id = passwordId)

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
                        textValue = passwordEntity.title ?: "", // if it is null, will print "" (nothing)
                        label = stringResource(R.string.title_web),
                        validators = listOf(Required(message = stringResource(R.string.title_web_is_required)))
                    ),
                    Field(
                        name = USER_FIELD_NAME,
                        textValue = passwordEntity.user ?: "",
                        label = stringResource(R.string.user),
                        validators = listOf(Required(message = stringResource(R.string.user_is_required)))
                    ),
                    Field(
                        name = PASSWORD_FIELD_NAME,
                        textValue = passwordEntity.password?: "",
                        label = stringResource(R.string.password),
                        isPassword = true,
                        validators = listOf(Required(message = stringResource(R.string.password_is_required)))
                    ),
                    Field(
                        name = EXTRA_INFO_FIELD_NAME,
                        textValue = passwordEntity.extraInfo?: "",
                        label = stringResource(R.string.extra_info),
                        validators = listOf(Required(message = stringResource(R.string.extra_info_is_required)))
                    )
                )
            )
            // Save button
            Row(
                modifier = Modifier.fillMaxSize().padding(end = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { //Actions for storing passwords
                        if (state.validate()) {
                            Log.i(
                                "MBK::EditPasswordScreen::EditPasswordForm",
                                "New form entry has been validated"
                            )
                            //Get values from fields in the form
                            val newPasswordEntryMap: Map<String, String> = state.getData()
                            // Logs the content of the form when save icon is pressed
                            newPasswordEntryMap.forEach { field ->
                                Log.d(
                                    "MBK::EditPasswordScreen::EditPasswordForm",
                                    "Field data: ${field.key}: ${field.value}"
                                )
                            }
                            //Store values in database
                            // Update existing password
                            getDbConnection(context = context).updatePassword(
                                passwordEntity = PasswordEntity(
                                    id = passwordId, // id needed in order to update the proper password entity
                                    title = newPasswordEntryMap[TITLE_FIELD_NAME],
                                    user = newPasswordEntryMap[USER_FIELD_NAME],
                                    password = newPasswordEntryMap[PASSWORD_FIELD_NAME],
                                    extraInfo = newPasswordEntryMap[EXTRA_INFO_FIELD_NAME],
                                )
                            )
                            Log.d(
                                "MBK::EditPasswordScreen::EditPasswordForm",
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