package es.apps.laos.mybunker

sealed class Screens(val route: String) {
    object Home: Screens("home_screen")
    object AddPassword: Screens("add_password_screen")
}