// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services.implementation

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.image.ImageURL
import com.aallam.openai.api.image.ImageVariation
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.model.Model
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.MessageMetadata
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.OpenAiService
import com.mohandass.botforge.settings.service.SharedPreferencesService
import okio.Source
import okio.source
import kotlin.time.Duration.Companion.seconds

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

        val config = OpenAIConfig(
            token = apiKey,
            logLevel = LogLevel.Info,
            timeout = Timeout(socket = sharedPreferencesService.getApiTimeout().seconds),
        )

        return OpenAI(config)
    }

    override suspend fun getAvailableModels(): List<Model> {
        logger.logVerbose(TAG, "getAvailableModels()")
        try {
            logger.logVerbose(TAG, "getAvailableModels()")
            return getClient().models()
        } catch (e: Exception) {
            logger.logError(TAG, "getAvailableModels() ${e.printStackTrace()}", e)
            throw e
        }
    }

    override suspend fun getChatCompletion(
        messages: List<Message>,
    ): Message {
        logger.logVerbose(TAG, "getChatCompletion() ${messages.size}")
        val chatMessages = messages.map { it.toChatMessage() }

        for (chatMessage in chatMessages) {
            logger.logVerbose(TAG, "getChatCompletion() ${chatMessage.content}")
        }

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId(sharedPreferencesService.getChatModel()),
            messages = chatMessages,
        )

        try {
            logger.logVerbose(TAG, "getChatCompletion() start request")
            val completion: ChatCompletion = getClient().chatCompletion(chatCompletionRequest)

            val metadata = MessageMetadata(
                openAiId = completion.id,
                finishReason = completion.choices[0].finishReason.toString(),
                promptTokens = completion.usage?.promptTokens,
                completionTokens = completion.usage?.completionTokens,
                totalTokens = completion.usage?.totalTokens,
            )

            // Update usage tokens
            sharedPreferencesService.incrementUsageTokens(completion.usage?.totalTokens ?: 0)

            return Message(
                text = completion.choices[0].message.content?.trim() ?: "",
                role = Role.BOT,
                metadata = metadata,
            )
        } catch (e: Exception) {
            logger.logError(TAG, "getChatCompletion() ${e.printStackTrace()}", e)
            throw e
        }
    }

    override suspend fun generateImage(
        prompt: String,
        n: Int,
        imageSize: ImageSize,
    ): List<ImageURL> {
        logger.logVerbose(TAG, "generateImage() $prompt")
        try {
            logger.logVerbose(TAG, "generateImage() start request")
            val images = getClient().imageURL( // or openAI.imageJSON
                creation = ImageCreation(
                    prompt = prompt,
                    n = n,
                    size = imageSize
                )
            )

            // Update usage image count
            when (imageSize) {
                ImageSize.is256x256 -> {
                    sharedPreferencesService.incrementUsageImageSmallCount(n)
                }

                ImageSize.is512x512 -> {
                    sharedPreferencesService.incrementUsageImageMediumCount(n)
                }

                ImageSize.is1024x1024 -> {
                    sharedPreferencesService.incrementUsageImageLargeCount(n)
                }
            }

            logger.logVerbose(TAG, "generateImage() $images")
            return images
        } catch (e: Exception) {
            logger.logError(TAG, "generateImage() ${e.printStackTrace()}", e)
            throw e
        }
    }

    @OptIn(BetaOpenAI::class)
    override suspend fun generateImageVariant(
        original: ByteArray,
        n: Int,
        imageSize: ImageSize,
    ): List<ImageURL> {
        logger.logVerbose(TAG, "generateImageVariant()")
        try {
            val source: Source = original.inputStream().source()

            val fileSource = FileSource(name = "original.png", source = source)
            val images = getClient().imageURL( // or openAI.imageJSON
                variation = ImageVariation(
                    image = fileSource,
                    n = n,
                    size = imageSize
                )
            )

            // Update usage image count
            when (imageSize) {
                ImageSize.is256x256 -> {
                    sharedPreferencesService.incrementUsageImageSmallCount(n)
                }

                ImageSize.is512x512 -> {
                    sharedPreferencesService.incrementUsageImageMediumCount(n)
                }

                ImageSize.is1024x1024 -> {
                    sharedPreferencesService.incrementUsageImageLargeCount(n)
                }
            }

            logger.logVerbose(TAG, "generateImageVariant() $images")
            return images
        } catch (e: Exception) {
            logger.logError(TAG, "generateImageVariant() ${e.printStackTrace()}", e)
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
