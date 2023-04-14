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
import com.mohandass.botforge.common.services.implementation.AndroidLogger
import com.mohandass.botforge.common.services.implementation.DisabledAnalytics
import com.mohandass.botforge.common.services.implementation.FirebaseAnalyticsImpl
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module to provide the common services like the database, logger, and Firebase
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

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
    fun provideLogger(): Logger = AndroidLogger()

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

    companion object {
        const val TAG = "AppModule"
    }
}
