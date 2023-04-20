// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.services.implementation

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.MessageMetadata
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.chat.services.OpenAiService
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.SharedPreferencesService

/**
 * An implementation of the OpenAiService interface
 *
 * CAUTION: DO NOT LOG API KEY
 */
class OpenAiServiceImpl private constructor(
    private val sharedPreferencesService: SharedPreferencesService,
    private val logger: Logger,
) :
    OpenAiService {

    private fun getClient(): OpenAI {
        val apiKey = sharedPreferencesService.getApiKey()

        if (apiKey == "") {
            logger.logError(TAG, "getClient() No API key found")
            throw Exception("No API key found")
        }

        return OpenAI(apiKey)
    }

    @OptIn(BetaOpenAI::class)
    override suspend fun getChatCompletion(
        messages: List<Message>,
        modelId: ModelId
    ): Message {
        logger.logVerbose(TAG, "getChatCompletion() ${messages.size}")
        val chatMessages = messages.map { it.toChatMessage() }

        for (chatMessage in chatMessages) {
            logger.logVerbose(TAG, "getChatCompletion() ${chatMessage.content}")
        }

        val chatCompletionRequest = ChatCompletionRequest(
            model = modelId,
            messages = chatMessages,
        )

        try {
            logger.logVerbose(TAG, "getChatCompletion() start request")
            val completion: ChatCompletion = getClient().chatCompletion(chatCompletionRequest)

            val metadata = MessageMetadata(
                openAiId = completion.id,
                finishReason = completion.choices[0].finishReason,
                promptTokens = completion.usage?.promptTokens,
                completionTokens = completion.usage?.completionTokens,
                totalTokens = completion.usage?.totalTokens,
            )

            // Update usage tokens
            sharedPreferencesService.incrementUsageTokens(completion.usage?.totalTokens ?: 0)

            return Message(
                text = completion.choices[0].message?.content?.trim() ?: "",
                role = Role.BOT,
                metadata = metadata,
            )
        } catch (e: Exception) {
            logger.logError(TAG, "getChatCompletion() ${e.printStackTrace()}", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "OpenAiService"

        @Volatile
        private var INSTANCE: OpenAiService? = null

        fun getInstance(
            sharedPreferencesService: SharedPreferencesService,
            logger: Logger,
        ): OpenAiService {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = OpenAiServiceImpl(sharedPreferencesService, logger)
                INSTANCE = instance
                return instance
            }
        }
    }
}