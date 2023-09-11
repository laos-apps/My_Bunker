package es.apps.laos.mybunker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import es.apps.laos.mybunker.screens.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.v(
                "MBK::MainActivity::onCreate",
                "Main Activity created"
            )
            // Set up navigation controller. See NavGraph.kt and Screens.kt
            val navController = rememberNavController()
            NavGraph(navController = navController)
        }
    }
}


