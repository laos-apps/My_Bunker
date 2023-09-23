package es.apps.laos.mybunker

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

// https://github.com/vinchamp77/Demo_RuntimePermission
// REQUIRED SINGLE PERMISSION METHODS
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiredSinglePermissionScreen(
    permission: String,
    permissionGranted: () -> Unit
) {
    Log.v(
        "MBK::PermissionManager::RequiredSinglePermissionScreen",
        "RequiredSinglePermissionScreen called"
    )

    var permissionStatusText by remember { mutableStateOf("") }
    val permissionState = rememberPermissionState(permission)

    if (permissionState.status.isGranted) {
        permissionStatusText = "Granted"
        permissionGranted()

    } else if (permissionState.status.shouldShowRationale) {
        permissionStatusText = "Denied"
        RequiredRationalPermissionDialog(permission)

    } else {
        permissionStatusText = "Not granted"
        RequiredLaunchPermissionDialog(permission, permissionState)

        // Automatically open permission request window. First, I prefer to show an info Alert Dialog to inform users
        /*SideEffect {
            permissionState.launchPermissionRequest()
        }*/
    }
    //Text("Required Permission status: $permissionStatusText")
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiredLaunchPermissionDialog(
    permission: String,
    permissionState: PermissionState
) {
    Log.v(
        "MBK::PermissionManager::RequiredLaunchPermissionDialog",
        "RequiredLaunchPermissionDialog called"
    )
    val openDialog = remember { mutableStateOf(true) }
    val showPermissionButton = remember { mutableStateOf(false) }

    // Gets info about what this permission does
    /* val context = LocalContext.current
    val permissionInfo = stringResource(
        context.packageManager.getPermissionInfo(permission, 0).labelRes
    )*/
    val alertDialogPermissionTitle = stringResource(R.string.required_permission)
    val permissionInfo = stringResource(R.string.write_external_storage_permission_info)

    if (openDialog.value) {
        AlertDialog(
            // when user clicks outside alert dialog
            onDismissRequest = {
                openDialog.value = false
                showPermissionButton.value = true
            },
            title = { Text(text = alertDialogPermissionTitle) },
            text = { Text(text = permissionInfo) },
            confirmButton = {
                Button(onClick = {
                    permissionState.launchPermissionRequest()
                }) {
                    Text(text = "Launch")
                }
            }
        )
    }
    if (showPermissionButton.value) {
        CustomButton(
            text = stringResource(R.string.grant_permission),
            onClick = { permissionState.launchPermissionRequest() })
    }
}

// User will be asked for going to app settings page
@Composable
fun RequiredRationalPermissionDialog(permission: String) {
    val showSettingsButton = remember { mutableStateOf(false) }
    val context = LocalContext.current
    // Gets info about what this permission does
    /*val permissionLabel = stringResource(
        context.packageManager.getPermissionInfo(permission, 0).labelRes
    )*/
    val alertDialogPermissionTitle = stringResource(R.string.required_permission)
    Log.v(
        "MBK::PermissionManager::RequiredRationalPermissionDialog",
        "RequiredRationalPermissionDialog called"
    )
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            // when user clicks outside alert dialog
            onDismissRequest = {
                openDialog.value = false
                showSettingsButton.value = true
            },
            title = { Text(text = alertDialogPermissionTitle) },
            text = { Text(text = stringResource(R.string.please_grant_write_permission)) },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    ContextCompat.startActivity(context, intent, null)
                }) {
                    Text(text = stringResource(R.string.go_to_app_settings))
                }
            }
        )
    }
    // Shows go to settings button when alert dialog is dismissed and permission is still denied
    if (showSettingsButton.value) {
        Text(stringResource(R.string.please_grant_write_permission))
        CustomButton(
            text = stringResource(R.string.go_to_app_settings),
            onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                ContextCompat.startActivity(context, intent, null)
            })
    }
}

// REQUIRED MULTIPLE PERMISSIONS METHODS
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiredMultiplePermissionScreen(
    permissions: List<String>
) {

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)
    val permissionStatusText by remember(multiplePermissionsState) {
        derivedStateOf {
            getPermissionStatusText(multiplePermissionsState)
        }
    }

    if (multiplePermissionsState.shouldShowRationale) {
        RequiredRationalPermissionsDialog(
            permissions
        )

    } else if (!multiplePermissionsState.allPermissionsGranted) {
        RequiredLaunchPermissionsDialog(
            permissions,
            multiplePermissionsState,
        )

        SideEffect {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Required Permission status: $permissionStatusText")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiredLaunchPermissionsDialog(
    permissions: List<String>,
    multiplePermissionState: MultiplePermissionsState,
) {
    val context = LocalContext.current
    var permissionLabels = ""
    permissions.forEachIndexed { index, permission ->
        val permissionLabel = stringResource(
            context.packageManager.getPermissionInfo(permission, 0).labelRes
        )
        permissionLabels += permissionLabel

        if (index < permission.count() - 1) {
            permissionLabels += "\n"
        }
    }

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Permission Required!") },
        text = { Text(text = permissionLabels) },
        confirmButton = {
            Button(onClick = {
                multiplePermissionState.launchMultiplePermissionRequest()
            }) {
                Text(text = "Launch")
            }
        },
    )
}

@Composable
fun RequiredRationalPermissionsDialog(
    permissions: List<String>
) {
    val context = LocalContext.current
    var permissionLabels = ""
    permissions.forEachIndexed { index, permission ->
        val permissionLabel = stringResource(
            context.packageManager.getPermissionInfo(permission, 0).labelRes
        )
        permissionLabels += permissionLabel

        if (index < permission.count() - 1) {
            permissionLabels += "\n"
        }
    }

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Permission Required!") },
        text = { Text(text = permissionLabels) },
        confirmButton = {
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                ContextCompat.startActivity(context, intent, null)
            }) {
                Text(text = "Go to settings")
            }
        },
    )
}

@OptIn(ExperimentalPermissionsApi::class)
fun getPermissionStatusText(multiplePermissionsState: MultiplePermissionsState): String {
    var permissionStatusText = "\n"
    for (permissionStatus in multiplePermissionsState.permissions) {
        val statusText = if (permissionStatus.status.isGranted) {
            "Granted\n"
        } else {
            "Denied\n"
        }
        permissionStatusText += "${permissionStatus.permission}: $statusText"
    }

    return permissionStatusText
}

// Button to ask for permission
@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}