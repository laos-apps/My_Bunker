package es.apps.laos.mybunker.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import es.apps.laos.mybunker.FormState
import es.apps.laos.mybunker.LanguageManager
import es.apps.laos.mybunker.ui.theme.MyBunkerTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        val languageManager: LanguageManager = LanguageManager()
        items(items = languageManager.languagesList) {
            LanguageSelectionMenuItem(languageName = it.languageName, flagEmojiCode = languageManager.countryCodeToEmojiFlag(it.languageKey))
        }
    }
}

@Composable
fun LanguageSelectionMenuItem(languageName: String, flagEmojiCode: String) {
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)) {
        Row() {
            // Flag
            Text(fontSize = 40.sp, text = flagEmojiCode)
            // Language name
            Text(fontSize = 40.sp, text = languageName)
        }
    }
}