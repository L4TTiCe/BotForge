// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.service.implementation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.model.PreferredHeader
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.settings.model.UserPreferences
import com.mohandass.botforge.settings.service.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Implementation of the PreferencesDataStore
 *
 * Uses the DataStore library to store the preferences.
 * Stores themes, dynamic colors, last successful sync, user generated content, shake to clear,
 * and shake to clear sensitivity.
 *
 * Maps stored data as a [UserPreferences] object.
 *
 * @param dataStore DataStore to store preferences
 * @param logger Logger to log errors
 */
class PreferencesDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : PreferencesDataStore {

    // Keys for the preferences
    private object PreferencesKeys {
        // Appearance
        val PREF_THEME = stringPreferencesKey(PreferencesDataStore.PREFERRED_THEME_KEY)
        val DYNAMIC_COLOR = booleanPreferencesKey(PreferencesDataStore.DYNAMIC_COLOR)
        val PREF_HEADER = stringPreferencesKey(PreferencesDataStore.PREFERRED_HEADER)

        // Sync
        val LAST_SUCCESSFUL_SYNC = longPreferencesKey(PreferencesDataStore.LAST_SUCCESSFUL_SYNC)
        val LAST_MODERATION_INDEX_PROCESSED =
            intPreferencesKey(PreferencesDataStore.LAST_MODERATION_INDEX_PROCESSED)

        // Settings
        val USER_GENERATED_CONTENT =
            booleanPreferencesKey(PreferencesDataStore.USER_GENERATED_CONTENT)
        val SHAKE_TO_CLEAR = booleanPreferencesKey(PreferencesDataStore.SHAKE_TO_CLEAR)
        val SHAKE_TO_CLEAR_SENSITIVITY =
            floatPreferencesKey(PreferencesDataStore.SHAKE_TO_CLEAR_SENSITIVITY)
        val AUTO_GENERATE_CHAT_TITLE =
            booleanPreferencesKey(PreferencesDataStore.AUTO_GENERATE_CHAT_TITLE)

        // Image Generation
        val ENABLE_IMAGE_GENERATION =
            booleanPreferencesKey(PreferencesDataStore.ENABLE_IMAGE_GENERATION)
    }

    /**
     * Get the user preferences flow.
     */
    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                logger.logError(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    override suspend fun updateTheme(preferredTheme: PreferredTheme) {
        logger.logVerbose(TAG, "updateTheme() preferredTheme: $preferredTheme")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREF_THEME] = preferredTheme.name
        }
    }

    override suspend fun setDynamicColor(newValue: Boolean) {
        logger.logVerbose(TAG, "setDynamicColor() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLOR] = newValue
        }
    }

    override suspend fun setPreferredHeader(newValue: PreferredHeader) {
        logger.logVerbose(TAG, "updateHeader() preferredHeader: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREF_HEADER] = newValue.name
        }
    }

    override suspend fun updateLastSuccessfulSync() {
        logger.logVerbose(TAG, "updateLastSuccessfulSync()")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SUCCESSFUL_SYNC] = System.currentTimeMillis()
        }
    }

    override suspend fun clearLastSuccessfulSync() {
        logger.logVerbose(TAG, "clearLastSuccessfulSync()")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SUCCESSFUL_SYNC] = 0L
        }
    }

    override suspend fun setLastModerationIndexProcessed(newValue: Int) {
        logger.logVerbose(TAG, "setLastModerationIndexProcessed() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_MODERATION_INDEX_PROCESSED] = newValue
        }
    }

    override suspend fun setUserGeneratedContent(newValue: Boolean) {
        logger.logVerbose(TAG, "setUserGeneratedContent() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_GENERATED_CONTENT] = newValue
        }
    }

    override suspend fun setShakeToClear(newValue: Boolean) {
        logger.logVerbose(TAG, "setShakeToClear() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHAKE_TO_CLEAR] = newValue
        }
    }

    override suspend fun setShakeToClearSensitivity(newValue: Float) {
        logger.logVerbose(TAG, "setShakeToClearSensitivity() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHAKE_TO_CLEAR_SENSITIVITY] = newValue
        }
    }

    override suspend fun setAutoGenerateChatTitle(newValue: Boolean) {
        logger.logVerbose(TAG, "setAutoGenerateChatTitle() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_GENERATE_CHAT_TITLE] = newValue
        }
    }

    override suspend fun setEnableImageGeneration(newValue: Boolean) {
        logger.logVerbose(TAG, "setEnableImageGeneration() newValue: $newValue")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_IMAGE_GENERATION] = newValue
        }
    }

    override suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    // Maps the preferences to a UserPreferences object
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Appearance
        val preferredTheme = PreferredTheme.valueOf(
            preferences[PreferencesKeys.PREF_THEME] ?: PreferredTheme.AUTO.name
        )
        val useDynamicColors = preferences[PreferencesKeys.DYNAMIC_COLOR] ?: true
        val preferredHeader = PreferredHeader.valueOf(
            preferences[PreferencesKeys.PREF_HEADER] ?: PreferredHeader.DEFAULT_HEADER.name
        )

        // Sync
        val lastSuccessfulSync = preferences[PreferencesKeys.LAST_SUCCESSFUL_SYNC] ?: 0L
        val lastModerationIndexProcessed =
            preferences[PreferencesKeys.LAST_MODERATION_INDEX_PROCESSED] ?: 0

        // Settings
        val enableUserGeneratedContent = preferences[PreferencesKeys.USER_GENERATED_CONTENT] ?: true
        val enableShakeToClear = preferences[PreferencesKeys.SHAKE_TO_CLEAR] ?: false
        val shakeToClearSensitivity = preferences[PreferencesKeys.SHAKE_TO_CLEAR_SENSITIVITY] ?: 0f
        val autoGenerateChatTitle = preferences[PreferencesKeys.AUTO_GENERATE_CHAT_TITLE] ?: true

        // Image Generation
        val enableImageGeneration = preferences[PreferencesKeys.ENABLE_IMAGE_GENERATION] ?: true

        return UserPreferences(
            preferredTheme = preferredTheme,
            useDynamicColors = useDynamicColors,
            preferredHeader = preferredHeader,
            lastSuccessfulSync = lastSuccessfulSync,
            enableUserGeneratedContent = enableUserGeneratedContent,
            enableShakeToClear = enableShakeToClear,
            shakeToClearSensitivity = shakeToClearSensitivity,
            lastModerationIndexProcessed = lastModerationIndexProcessed,
            autoGenerateChatTitle = autoGenerateChatTitle,
            enableImageGeneration = enableImageGeneration
        )
    }

    companion object {
        private const val TAG = "PreferencesDataStoreImpl"
    }
}
