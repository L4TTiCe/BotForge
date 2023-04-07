// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.common.services.LocalDatabase
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.implementation.AndroidLogger
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
}
