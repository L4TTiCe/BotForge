package com.mohandass.botforge.chat.services

import com.aallam.openai.api.model.ModelId
import com.mohandass.botforge.chat.model.Message

interface OpenAiService {

    suspend fun getChatCompletion(
        messages: List<Message>,
        modelId: ModelId = ModelId("gpt-3.5-turbo")
    ): Message
}
