package com.mohandass.botforge.sync

import com.mohandass.botforge.common.service.LocalDatabase
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.model.service.BotServiceImpl
import com.mohandass.botforge.sync.service.FirebaseDatabaseServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SyncModule {
    @Provides
    @Singleton
    fun provideBotDao(
        localDatabase: LocalDatabase
    ): BotDao = localDatabase.botDao()

    @Provides
    @Singleton
    fun provideBotServiceImpl(
        botDao: BotDao
    ) = BotServiceImpl(botDao)

    @Provides
    @Singleton
    fun provideFirebaseDatabaseService(): FirebaseDatabaseServiceImpl
    = FirebaseDatabaseServiceImpl()
}