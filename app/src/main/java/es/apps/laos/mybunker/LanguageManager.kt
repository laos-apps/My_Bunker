package es.apps.laos.mybunker

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import es.apps.laos.mybunker.screens.DEFAULT_LANGUAGE_KEY
import java.util.Locale

// Codes for language keys extracted from ISO-3166-1
class LanguageManager {
    var languagesList: ArrayList<Language> = ArrayList()
    init {
        // Spanish
        languagesList.add(
            Language(
                languageKey = "ES",
                languageName = "Español",
            )
        )
        // English
        languagesList.add(
            Language(
                languageKey = "GB",
                languageName = "English",
            )
        )
        // French
        languagesList.add(
            Language(
                languageKey = "FR",
                languageName = "Français",
            )
        )
        // German
        languagesList.add(
            Language(
                languageKey = "DE",
                languageName = "Deutsch",
            )
        )
        // Chinese (chino mandarín)
        languagesList.add(
            Language(
                languageKey = "CN",
                languageName = "中文",
            )
        )
        // Italian
        languagesList.add(
            Language(
                languageKey = "IT",
                languageName = "Italiano",
            )
        )
        // Portuguese
        languagesList.add(
            Language(
                languageKey = "PT",
                languageName = "Português",
            )
        )
        // Arabic
        languagesList.add(
            Language(
                languageKey = "AE",
                languageName = "العربية",
            )
        )
    }

    fun countryCodeToEmojiFlag(countryCode: String): String {
        return countryCode
            .uppercase(Locale.US)
            .map { char ->
                Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
            }
            .map { codePoint ->
                Character.toChars(codePoint)
            }
            .joinToString(separator = "") { charArray ->
                String(charArray)
            }
    }

    @Composable
    fun getUserLanguageFromSettings(context: Context): String{
        // Get userLanguageKey from preferences
        val settingsManager = SettingsManager(context)
        var userLanguageKey = settingsManager.getLanguageFromDatastore().collectAsState(initial = "").value
        Log.d(
            "MBK::LanguageSettingsScreen::LanguageSelectionMenu",
            "User language key is: $userLanguageKey"
        )
        // If language is still not set, GB will be chosen by default
        if (userLanguageKey.isEmpty()) userLanguageKey = DEFAULT_LANGUAGE_KEY
        return userLanguageKey
    }
}