package com.mohandass.botforge.sync

import com.mohandass.botforge.common.service.LocalDatabase
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.model.service.BotService
import com.mohandass.botforge.sync.model.service.FirestoreService
import com.mohandass.botforge.sync.model.service.implementation.BotServiceImpl
import com.mohandass.botforge.sync.model.service.implementation.FirebaseDatabaseServiceImpl
import com.mohandass.botforge.sync.model.service.implementation.FirestoreServiceImpl
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