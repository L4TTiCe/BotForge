// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.common.services.LocalDatabase
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.OpenAiService
import com.mohandass.botforge.common.services.implementation.DisabledAnalytics
import com.mohandass.botforge.common.services.implementation.FirebaseAnalyticsImpl
import com.mohandass.botforge.common.services.implementation.FirebaseCrashlyticsLogger
import com.mohandass.botforge.common.services.implementation.OpenAiServiceImpl
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Module to provide the common services like the database, logger, and Firebase
 */
@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideLocalDatabase(
        app: Application
    ): LocalDatabase = LocalDatabase.getInstance(app)

    @Provides
    @Singleton
    fun provideLogger(): Logger = FirebaseCrashlyticsLogger()

    @Provides
    @Singleton
    fun provideOpenAiService(
        sharedPreferencesService: SharedPreferencesService,
        logger: Logger,
    ): OpenAiService = OpenAiServiceImpl.getInstance(sharedPreferencesService, logger)

    @Provides
    @Singleton
    fun provideAnalyticsService(
        logger: Logger,
        preferences: SharedPreferencesService
    ): Analytics {
        // If the user has opted out of analytics, return a dummy implementation
        if (preferences.getAnalyticsOptOut()) {
            logger.log(TAG, "No Analytics")
            return DisabledAnalytics()
        }

        logger.log(TAG, "Analytics Enabled")
        return FirebaseAnalyticsImpl()
    }

    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    companion object {
        const val TAG = "AppModule"
    }
}
