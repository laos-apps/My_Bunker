package es.apps.laos.mybunker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

class Field(
    val name: String,
    val label: String = "",
    private val isPassword: Boolean = false,
    val validators: List<Validator>
) {
    var text: String by mutableStateOf("")
    var lbl: String by mutableStateOf(label)
    var hasError: Boolean by mutableStateOf(false)
    var passwordVisible by mutableStateOf(false)

    fun clear() {
        text = ""
    }

    private fun showError(error: String) {
        hasError = true
        lbl = error
    }

    private fun hideError() {
        lbl = label
        hasError = false
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content() {
        if (!isPassword) {
            TextField(
                value = text,
                isError = hasError,
                label = { Text(text = lbl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onValueChange = { value ->
                    hideError()
                    text = value
                }
            )
        } else { // if it is a password
            TextField(
                value = text,
                isError = hasError,
                label = { Text(text = lbl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onValueChange = { value ->
                    hideError()
                    text = value
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(), // Hides text
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
        }
    }


    fun validate(): Boolean {
        return validators.map {
            when (it) {
                is Required -> {
                    if (text.isEmpty()) {
                        showError(it.message)
                        return@map false
                    }
                    true
                }

                is Regex -> {
                    if (!it.regex.toRegex().containsMatchIn(text)) {
                        showError(it.message)
                        return@map false
                    }
                    true
                }
            }
        }.all { it }
    }
}