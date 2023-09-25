package es.apps.laos.mybunker.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import es.apps.laos.mybunker.PasswordEntity
import es.apps.laos.mybunker.R
import es.apps.laos.mybunker.R.color.purple_200
import es.apps.laos.mybunker.RequiredSinglePermissionScreen
import es.apps.laos.mybunker.ui.theme.MyBunkerTheme
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


// COMPOSABLE METHODS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfExportScreen(navController: NavController) {
    Log.d(
        "MBK::PdfExportScreen::PdfExportScreen",
        "Opening PDF Export Screen"
    )
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
                                    contentDescription = stringResource(R.string.go_home)
                                )
                            }
                        },
                        title = { Text(text = stringResource(R.string.export_to_pdf)) },
                    )
                },
                content = { paddingValues ->
                    // In order to avoid that content is shown behind the top bar we have to pass PaddingValues
                    Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                        PdfExporterScheme()
                    }
                }
            )
        }
    }
}

@Composable
fun PdfExporterScheme() {
    Log.d(
        "MBK::PdfExportScreen::PdfExporterScheme",
        "PdfExporterScheme called"
    )
    val isWritePermissionGranted = remember { mutableStateOf(false) }
    // First ask for permissions
    RequiredSinglePermissionScreen(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
        permissionGranted = { isWritePermissionGranted.value = true })
    if (isWritePermissionGranted.value) {
        ExportPdfButton()
    }
}

@Composable
fun ExportPdfButton() {
    val context: Context = LocalContext.current
    Button(
        onClick = {
            createPdf(
                passwordList = getPasswordList(context),
                context = context
            )
        }
    ) {
        Text(text = stringResource(R.string.export))
    }
}
fun createPdf(passwordList: ArrayList<PasswordEntity>, context: Context) {
    Log.v(
        "MBK::PdfExporter::CreatePdf",
        "CreatePdf called"
    )

    // Declaring starting points
    val startX = 20F
    val startY = 20F
    var x = 0F
    var y = 0F

    // Declaring width and height for our PDF file.
    val pageHeight = 1120
    val pageWidth = 792

    // Create a new document
    val pdfDocument = PdfDocument()
    // two variables for paint "paint" is used
    // for drawing shapes and we will use "title"
    // for adding text in our PDF file.
    val title = Paint()

    // Create a page description
    val pageInfo: PdfDocument.PageInfo =
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

    // Start a page
    val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)

    // creating a variable for canvas
    // from our page of PDF.
    val canvas: Canvas = page.canvas
    // below line is used for adding typeface for
    // our text which we will be adding in our PDF file.
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

    // below line is used for setting text size
    // which we will be displaying in our PDF file.
    title.textSize = 15F

    // below line is sued for setting color
    // of our text inside our PDF file.
    title.color = ContextCompat.getColor(context, purple_200)

    // below line is used to draw text in our PDF file.
    // the first parameter is our text, second parameter
    // is position from start, third parameter is position from top
    // and then we are passing our variable of paint which is title.
    canvas.drawText(context.getString(R.string.my_bunker), startX, startY, title)
    y+=10F
    canvas.drawText(context.getString(R.string.list_of_passwords), startX, startY+y, title)
    y+=20F

    // similarly we are creating another text and in this
    // we are aligning this text to center of our PDF file.
    // similarly we are creating another text and in this
    // we are aligning this text to center of our PDF file.
    title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    title.color = ContextCompat.getColor(context, purple_200)
    title.textSize = 15f

    // below line is used for setting
    // our text to center of PDF.

    // below line is used for setting
    // our text to center of PDF.
    //title.textAlign = Paint.Align.CENTER
    for (password in passwordList) {
        canvas.drawText("Title/Web: ${password.title}", startX, startY+y, title)
        y+=10F
        canvas.drawText("User: ${password.user}", startX, startY+y, title)
        y+=10F
        canvas.drawText("Password: ${password.password}", startX, startY+y, title)
        y+=10F
        canvas.drawText("Extra info: ${password.extraInfo}", startX, startY+y, title)
        y+=10F
    }

    // after adding all attributes to our
    // PDF file we will be finishing our page.
    pdfDocument.finishPage(page)
    // add more pages

    // below line is used to set the name of
    // our PDF file and its path.
    // below line is used to set the name of
    // our PDF file and its path.
    // Store files in Downloads
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        context.getString(
            R.string.my_bunker_pdf
        )
    )
    Log.i(
        "MBK::PdfExporter::CreatePdf",
        "File exists?: ${file.exists()}\nFile path: ${file.absolutePath}"
    )
    try {
        // after creating a file name we will write our PDF file to that location.
        pdfDocument.writeTo(FileOutputStream(file))
        Log.i(
            "MBK::PdfExporter::CreatePdf",
            "PDF created successfully"
        )
    } catch (e: IOException) {
        // below line is used
        // to handle error
        Log.e(
            "MBK::PdfExporter::CreatePdf::IOException",
            "Message: ${e.message}\nCause: ${e.cause}"
        )

    }
    // after storing our pdf to that
    // location we are closing our PDF file.
    pdfDocument.close()
    // Try to open file once is created
    val intent = Intent(Intent.ACTION_VIEW)
    val authority = "es.apps.laos.mybunker.fileprovider"
    val uri = FileProvider.getUriForFile(context,authority ,file)
    intent.setDataAndType(uri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(context, intent, null)
}