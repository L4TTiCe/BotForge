// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge

import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.PreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppState(
        preferencesDataStore: PreferencesDataStore,
        logger: Logger,
    ): AppState = AppState(preferencesDataStore, logger)
}
