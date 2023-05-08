// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.image.ImageURL
import com.aallam.openai.api.model.Model
import com.mohandass.botforge.chat.model.Message

/**
 * An interface to represent the OpenAI API
 */
interface OpenAiService {

    suspend fun getAvailableModels(): List<Model>

    suspend fun getChatCompletion(
        messages: List<Message>,
    ): Message

    @OptIn(BetaOpenAI::class)
    suspend fun generateImage(
        prompt: String,
        n: Int = 1,
        imageSize: ImageSize = ImageSize.is256x256,
    ): List<ImageURL>

    @OptIn(BetaOpenAI::class)
    suspend fun generateImageVariant(
        original: ByteArray,
        n: Int,
        imageSize: ImageSize,
    ): List<ImageURL>
}
