// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat

import com.mohandass.botforge.AppState
import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.repositories.ActiveMessagesRepository
import com.mohandass.botforge.chat.repositories.ActivePersonaRepository
import com.mohandass.botforge.chat.repositories.PersonaRepository
import com.mohandass.botforge.chat.services.implementation.ChatServiceImpl
import com.mohandass.botforge.common.services.LocalDatabase
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.sync.service.BotService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module to provide services related to the chat, persona, and OpenAi
 */
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
        personaDao: PersonaDao,
        logger: Logger,
    ) = PersonaRepository(personaDao, logger)

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

    @Provides
    @Singleton
    fun provideActivePersonaRepository(
        botService: BotService
    ) = ActivePersonaRepository(botService)

    @Provides
    @Singleton
    fun provideActiveMessagesRepository(
        activePersonaRepository: ActivePersonaRepository,
        appState: AppState
    ) = ActiveMessagesRepository(
        activePersonaRepository = activePersonaRepository,
        appState = appState
    )
}
