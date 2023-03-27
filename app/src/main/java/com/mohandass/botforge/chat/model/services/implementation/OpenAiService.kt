package com.mohandass.botforge.chat.model.services.implementation

import android.util.Log
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.MessageMetadata
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.chat.model.services.OpenAiService
import com.mohandass.botforge.settings.model.service.SharedPreferencesService

class OpenAiServiceImpl private constructor(private val sharedPreferencesService: SharedPreferencesService) :
    OpenAiService {

    private fun getClient(): OpenAI {
        val apiKey = sharedPreferencesService.getApiKey()
        Log.v(TAG, "getClient() |$apiKey|")

        if (apiKey == "") {
            Log.e(TAG, "getClient() No API key found")
            throw Throwable("No API key found")
        }

        return OpenAI(apiKey)
    }

    @OptIn(BetaOpenAI::class)
    override suspend fun getChatCompletion(
        messages: List<Message>,
        modelId: ModelId
    ): Message {
        Log.v(TAG, "getChatCompletion() ${messages.size}")
        val chatMessages = messages.map { it.toChatMessage() }

        for (chatMessage in chatMessages) {
            Log.v(TAG, "getChatCompletion() ${chatMessage.content}")
        }

        val chatCompletionRequest = ChatCompletionRequest(
            model = modelId,
            messages = chatMessages,
        )

        try {
            Log.v(TAG, "getChatCompletion() start request")
            val completion: ChatCompletion = getClient().chatCompletion(chatCompletionRequest)
            Log.v(TAG, "getChatCompletion() ${completion.choices[0].message?.content}")

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
            Log.v("OpenAiService", "getChatCompletion() ${e.printStackTrace()}")
            throw e
        }
    }

    companion object {
        private const val TAG = "OpenAiService"

        @Volatile
        private var INSTANCE: OpenAiService? = null

        fun getInstance(sharedPreferencesService: SharedPreferencesService): OpenAiService {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = OpenAiServiceImpl(sharedPreferencesService)
                INSTANCE = instance
                return instance
            }
        }
    }
}