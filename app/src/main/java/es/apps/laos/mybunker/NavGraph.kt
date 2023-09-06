package es.apps.laos.mybunker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph (navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    )
    {
        composable(route = Screens.Home.route){
            HomeScreen(navController = navController)
        }
        composable(route = Screens.AddPassword.route){
           AddPasswordScreen(navController = navController)
        }
    }
}