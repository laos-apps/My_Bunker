package es.apps.laos.mybunker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import java.util.Locale

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
}