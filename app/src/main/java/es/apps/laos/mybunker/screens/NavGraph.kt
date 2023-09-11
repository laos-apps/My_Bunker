package es.apps.laos.mybunker.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    )
    {
        // Main screen
        composable(
            route = Screens.Home.route
        ) {
            HomeScreen(navController = navController)
        }
        // Screen for adding new passwords
        composable(
            route = Screens.AddPassword.route
        ) {
            AddPasswordScreen(navController = navController)
        }
        // Screen for editing passwords
        composable(
            route = Screens.EditPassword.route.plus("/{passwordId}"),
            // Passing selected password id
            arguments = listOf(navArgument("passwordId") { type = NavType.IntType })
        ) {
                backStackEntry ->
            val passwordId = backStackEntry.arguments?.getInt("passwordId")?: -1 // if it is null, will get -1
            EditPasswordScreen(navController = navController, passwordId = passwordId)
        }
        // Dropdown menu screens
        // Screen for configuring language
        composable(
            route = Screens.LanguageSettings.route
        ) {
            LanguageSettingsScreen(navController = navController)
        }
    }
}