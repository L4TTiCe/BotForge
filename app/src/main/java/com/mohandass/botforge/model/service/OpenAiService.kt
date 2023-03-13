package com.mohandass.botforge.model.service

import com.aallam.openai.api.model.ModelId
import com.mohandass.botforge.model.Message

interface OpenAiService {

    suspend fun getChatCompletion(
        messages: List<Message>,
        modelId: ModelId = ModelId("gpt-3.5-turbo")
    ): Message
}
