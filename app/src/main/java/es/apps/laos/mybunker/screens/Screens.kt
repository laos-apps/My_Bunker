package es.apps.laos.mybunker.screens

sealed class Screens(val route: String) {
    object Home: Screens("home_screen")
    object AddPassword: Screens("add_password_screen")
    object EditPassword: Screens("edit_password_screen")
    object PdfExport: Screens("pdf_export_screen")
}