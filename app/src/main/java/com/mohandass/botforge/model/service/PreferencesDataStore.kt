package com.mohandass.botforge.model.service

import com.mohandass.botforge.model.preferences.PreferredTheme
import com.mohandass.botforge.model.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun fetchInitialPreferences(): UserPreferences
    suspend fun updateTheme(preferredTheme: PreferredTheme)
    suspend fun setDynamicColor(newValue: Boolean)

    companion object {
        const val PREFERENCES_NAME = "botforge_preferences"
        const val PREFERRED_THEME_KEY = "theme"
        const val DYNAMIC_COLOR = "dynamic_color"
    }
}

