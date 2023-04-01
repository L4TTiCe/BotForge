package com.mohandass.botforge.sync

import com.mohandass.botforge.common.services.LocalDatabase
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.service.BotService
import com.mohandass.botforge.sync.service.FirestoreService
import com.mohandass.botforge.sync.service.implementation.BotServiceImpl
import com.mohandass.botforge.sync.service.implementation.FirebaseDatabaseServiceImpl
import com.mohandass.botforge.sync.service.implementation.FirestoreServiceImpl
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
        botDao: BotDao,
        logger: Logger
    ): BotService = BotServiceImpl(botDao, logger)

    @Provides
    @Singleton
    fun provideFirebaseDatabaseService(
        logger: Logger
    ): FirebaseDatabaseServiceImpl = FirebaseDatabaseServiceImpl(logger)

    @Provides
    @Singleton
    fun provideFirestoreService(
        logger: Logger
    )
            : FirestoreService = FirestoreServiceImpl(logger)
}