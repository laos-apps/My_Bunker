package es.apps.laos.mybunker.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import es.apps.laos.mybunker.LanguageManager
import es.apps.laos.mybunker.SettingsManager
import es.apps.laos.mybunker.ui.theme.MyBunkerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var userLanguageKey: String = ""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsScreen(navController: NavController) {
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
                            // Icon button for going back
                            IconButton(onClick = {
                                navController.navigate(Screens.Home.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Go home"
                                )
                            }
                        },
                        title = { Text(text = "Select language") },
                    )
                },
                content = {
                    // In order to avoid that content is shown behind the top bar we have to pass PaddingValues
                    LanguageSelectionMenu(paddingValues = it)
                }
            )
        }
    }
}

@Composable
fun LanguageSelectionMenu(paddingValues: PaddingValues) {
    // Get current context
    val context: Context = LocalContext.current
    // Get user language from settings
    var userLanguageKey: String by remember { mutableStateOf("") }
    // Get userLanguageKey from preferences
    val settingsManager = SettingsManager(context)
    userLanguageKey = settingsManager.getLanguageFromDatastore().collectAsState(initial = "").value
    Log.d(
        "MBK::LanguageSettingsScreen::LanguageSelectionMenu",
        "User language key is: $userLanguageKey"
    )
    // If language is still not set, GB will be chosen by default
    if (userLanguageKey.isEmpty()) userLanguageKey = DEFAULT_LANGUAGE_KEY

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {

        items(items = LanguageManager().languagesList) {
            LanguageSelectionMenuItem(
                languageKey = it.languageKey,
                languageName = it.languageName,
                flagEmojiCode = LanguageManager().countryCodeToEmojiFlag(it.languageKey),
                isSelected = (it.languageKey == userLanguageKey)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionMenuItem(
    languageKey: String,
    languageName: String,
    flagEmojiCode: String,
    isSelected: Boolean = false
) {
    // Gets context for composable methods
    val context: Context = LocalContext.current
    // Color for indicating selected language
    var colorCard: Color = Color.Cyan
    if (isSelected) colorCard =
        Color.Red // If language card matches with selected language, will be differently coloured
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorCard,
        ),
        onClick = {
            // Update user language in datastore
            val settingsManager = SettingsManager(context)
            CoroutineScope(Dispatchers.IO).launch {
                settingsManager.saveLanguageToDatastore(languageKey = languageKey) // Try to save Spanish
                Log.v(
                    "MBK::LanguageSettingsScreen::LanguageSettingsScreen",
                    "Language clicked: $languageKey-$languageName"
                )
            }
        }) {
        Row {
            // Flag
            Text(fontSize = 40.sp, text = flagEmojiCode)
            // Language name
            Text(fontSize = 40.sp, text = languageName)
        }
    }
}