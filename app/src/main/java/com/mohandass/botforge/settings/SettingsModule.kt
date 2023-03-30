package com.mohandass.botforge.settings

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
import com.mohandass.botforge.settings.model.service.implementation.PreferencesDataStoreImpl
import com.mohandass.botforge.settings.model.service.implementation.SharedPreferencesServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(
    name = PreferencesDataStore.PREFERENCES_NAME,
)

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @Provides
    @Singleton
    fun provideDataStore(
        app: Application
    ) = SharedPreferencesServiceImpl.getInstance(app)

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        logger: Logger,
        app: Application
    ): PreferencesDataStore = PreferencesDataStoreImpl(app.dataStore, logger)
}
