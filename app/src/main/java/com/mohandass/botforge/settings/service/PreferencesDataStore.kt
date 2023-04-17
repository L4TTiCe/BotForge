// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.service

import com.mohandass.botforge.settings.model.PreferredHeader
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.settings.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the PreferencesDataStore
 */
interface PreferencesDataStore {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun fetchInitialPreferences(): UserPreferences

    // Appearance
    suspend fun updateTheme(preferredTheme: PreferredTheme)
    suspend fun setDynamicColor(newValue: Boolean)
    suspend fun setPreferredHeader(newValue: PreferredHeader)

    //Sync
    suspend fun updateLastSuccessfulSync()
    suspend fun clearLastSuccessfulSync()

    //Settings
    suspend fun setUserGeneratedContent(newValue: Boolean)
    suspend fun setShakeToClear(newValue: Boolean)
    suspend fun setShakeToClearSensitivity(newValue: Float)
    suspend fun setLastModerationIndexProcessed(newValue: Int)
    suspend fun setAutoGenerateChatTitle(newValue: Boolean)

    companion object {
        const val PREFERENCES_NAME = "botforge_preferences"
        const val PREFERRED_THEME_KEY = "theme"
        const val DYNAMIC_COLOR = "dynamic_color"
        const val PREFERRED_HEADER = "header"
        const val LAST_SUCCESSFUL_SYNC = "last_successful_sync"
        const val USER_GENERATED_CONTENT = "user_generated_content"
        const val SHAKE_TO_CLEAR = "shake_to_clear"
        const val SHAKE_TO_CLEAR_SENSITIVITY = "shake_to_clear_sensitivity"
        const val LAST_MODERATION_INDEX_PROCESSED = "last_moderation_index_processed"
        const val AUTO_GENERATE_CHAT_TITLE = "auto_generate_chat_title"
    }
}
