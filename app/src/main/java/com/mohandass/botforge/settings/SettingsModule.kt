// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.PreferencesDataStore
import com.mohandass.botforge.settings.service.implementation.PreferencesDataStoreImpl
import com.mohandass.botforge.settings.service.implementation.SharedPreferencesServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Preferences DataStore
private val Context.dataStore by preferencesDataStore(
    name = PreferencesDataStore.PREFERENCES_NAME,
)

// Provides SharedPreferences and PreferencesDataStore
@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        app: Application
    ) = SharedPreferencesServiceImpl.getInstance(app)

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        logger: Logger,
        app: Application
    ): PreferencesDataStore = PreferencesDataStoreImpl(app.dataStore, logger)
}
