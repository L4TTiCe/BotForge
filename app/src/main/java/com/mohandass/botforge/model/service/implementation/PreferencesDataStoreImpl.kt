package com.mohandass.botforge.model.service.implementation

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.mohandass.botforge.model.preferences.PreferredTheme
import com.mohandass.botforge.model.preferences.UserPreferences
import com.mohandass.botforge.model.service.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException


class PreferencesDataStoreImpl(private val dataStore: DataStore<Preferences>): PreferencesDataStore {
    private object PreferencesKeys {
        val PREF_THEME = stringPreferencesKey(PreferencesDataStore.PREFERRED_THEME_KEY)
        val DYNAMIC_COLOR = booleanPreferencesKey(PreferencesDataStore.DYNAMIC_COLOR)
    }

    /**
     * Get the user preferences flow.
     */
    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    override suspend fun updateTheme(preferredTheme: PreferredTheme) {
        Log.v(TAG, "updateTheme() preferredTheme: $preferredTheme")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREF_THEME] = preferredTheme.name
        }
    }

    override suspend fun setDynamicColor(newValue: Boolean) {
        Log.v(TAG, "setDynamicColor() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLOR] = newValue
        }
    }

    override suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val preferredTheme = PreferredTheme.valueOf(
            preferences[PreferencesKeys.PREF_THEME] ?: PreferredTheme.AUTO.name
        )
        val useDynamicColors = preferences[PreferencesKeys.DYNAMIC_COLOR] ?: true
        return UserPreferences(preferredTheme, useDynamicColors)
    }

    companion object {
        private const val TAG = "PreferencesDataStoreImpl"
    }
}