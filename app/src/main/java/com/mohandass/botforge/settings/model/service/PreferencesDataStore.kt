package com.mohandass.botforge.settings.model.service

import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.settings.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun fetchInitialPreferences(): UserPreferences
    suspend fun updateTheme(preferredTheme: PreferredTheme)
    suspend fun setDynamicColor(newValue: Boolean)
    suspend fun updateLastSuccessfulSync()

    companion object {
        const val PREFERENCES_NAME = "botforge_preferences"
        const val PREFERRED_THEME_KEY = "theme"
        const val DYNAMIC_COLOR = "dynamic_color"
        const val LAST_SUCCESSFUL_SYNC = "last_successful_sync"
    }
}

