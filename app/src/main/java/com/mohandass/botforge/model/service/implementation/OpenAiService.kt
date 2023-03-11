package com.mohandass.botforge.model.service.implementation

import android.util.Log
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.MessageMetadata
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.model.service.DataStoreService

class OpenAiService private constructor(private val dataStoreService: DataStoreService) {

    private fun getClient(): OpenAI {
        return OpenAI(dataStoreService.getApiKey())
    }

    suspend fun logModels() {
        val models = getClient().models()
        for (model in models) {
            Log.v("OpenAiService", "logModels() ${model.id}")
        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun getChatCompletion(
        messages: List<Message>,
        modelId: ModelId = ModelId("gpt-3.5-turbo")
    ): Message {
        Log.v("OpenAiService", "getChatCompletion() ${messages.size}")
        val chatMessages = messages.map { it.toChatMessage() }

        for (chatMessage in chatMessages) {
            Log.v("OpenAiService", "getChatCompletion() ${chatMessage.content}")
        }

        val chatCompletionRequest = ChatCompletionRequest(
            model = modelId,
            messages = chatMessages,
        )

        val completion: ChatCompletion = getClient().chatCompletion(chatCompletionRequest)
        Log.v("OpenAiService", "getChatCompletion() ${completion.choices[0].message?.content}")

        val metadata = MessageMetadata(
            openAiId = completion.id,
            finishReason = completion.choices[0].finishReason,
            promptTokens = completion.usage?.promptTokens,
            completionTokens = completion.usage?.completionTokens,
            totalTokens = completion.usage?.totalTokens,
        )

        return Message(
            text = completion.choices[0].message?.content ?: "",
            role = Role.BOT,
            metadata = metadata,
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: OpenAiService? = null

        fun getInstance(dataStoreService: DataStoreService): OpenAiService {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = OpenAiService(dataStoreService)
                INSTANCE = instance
                return instance
            }
        }
    }
}