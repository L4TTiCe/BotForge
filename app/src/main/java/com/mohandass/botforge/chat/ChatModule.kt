package com.mohandass.botforge.chat

import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.services.OpenAiService
import com.mohandass.botforge.chat.services.implementation.ChatServiceImpl
import com.mohandass.botforge.chat.services.implementation.OpenAiServiceImpl
import com.mohandass.botforge.chat.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.common.services.LocalDatabase
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {
    @Provides
    @Singleton
    fun providePersonaService(
        localDatabase: LocalDatabase
    ): PersonaDao = localDatabase.personaService()

    @Provides
    @Singleton
    fun providePersonaServiceImpl(
        personaDao: PersonaDao
    ) = PersonaServiceImpl(personaDao)

    @Provides
    @Singleton
    fun provideOpenAiService(
        sharedPreferencesService: SharedPreferencesService,
        logger: Logger,
    ): OpenAiService = OpenAiServiceImpl.getInstance(sharedPreferencesService, logger)

    @Provides
    @Singleton
    fun provideChatDao(
        localDatabase: LocalDatabase
    ): ChatDao = localDatabase.chatDao()

    @Provides
    @Singleton
    fun provideChatServiceImpl(
        chatDao: ChatDao,
        logger: Logger,
    ) = ChatServiceImpl(chatDao, logger)
}
