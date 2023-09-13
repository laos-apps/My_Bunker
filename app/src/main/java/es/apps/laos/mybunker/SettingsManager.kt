package es.apps.laos.mybunker

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



// Data store saves key-value data
class SettingsManager (private val context: Context) {
    companion object{
        // Reading mbk_settings to get user preferences
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mbk_settings")
        private val selectedLanguagePreferenceKey = stringPreferencesKey("language_key")
    }

    suspend fun saveLanguageToDatastore(languageKey: String) {
        context.dataStore.edit { preferences ->
            Log.v(
                "MBK::SettingsManager::saveLanguageToDatastore",
                "Language to save: $languageKey"
            )
            preferences[selectedLanguagePreferenceKey] = languageKey
        }
    }
    fun getLanguageFromDatastore(): Flow<String> = context.dataStore.data
        .map { preferences ->
            Log.v(
                "MBK::SettingsManager::getLanguageFromDatastore",
                "Language returned: ${preferences[selectedLanguagePreferenceKey]}"
            )
            preferences[selectedLanguagePreferenceKey] ?: ""
        }
}