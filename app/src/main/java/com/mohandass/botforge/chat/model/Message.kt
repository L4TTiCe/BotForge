// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatMessage
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import java.util.*

/**
 * A data class to represent a Message
 *
 * This class is used to represent a Message in the Chat
 */
data class Message(
    val text: String = "",
    val role: Role = Role.USER,
    val uuid: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    var metadata: MessageMetadata? = null,
) {
    override fun toString(): String {
        return "Message(text='$text', role=$role, uuid='$uuid')"
    }

    @OptIn(BetaOpenAI::class)
    fun toChatMessage(): ChatMessage {
        return ChatMessage(
            content = text,
            role = role.toChatRole(),
        )
    }

    companion object {
        @OptIn(BetaOpenAI::class)
        fun from(message: ChatMessage): Message {
            val text = message.content
            var nonNullText = ""

            if (text != null) {
                nonNullText = text
            }
            return Message(
                text = nonNullText,
                role = Role.from(message.role),
                uuid = UUID.randomUUID().toString(),
            )
        }

        fun from(message: MessageE): Message {
            return Message(
                text = message.text,
                role = message.role,
                uuid = message.uuid,
                timestamp = message.timestamp,
                isActive = true,
                metadata = null,
            )
        }

        class MessageProvider : PreviewParameterProvider<Message> {
            override val values: Sequence<Message>
                get() = sequenceOf(
                    Message(
                        text = "Hello",
                        role = Role.USER,
                        uuid = UUID.randomUUID().toString(),
                        timestamp = System.currentTimeMillis(),
                        isActive = true,
                        metadata = MessageMetadata(
                            openAiId = "openai-id",
                            finishReason = "finish-reason",
                            promptTokens = 150,
                            completionTokens = 150,
                            totalTokens = 300,
                        ),
                    ),
                    Message(
                        text = "Hello",
                        role = Role.BOT,
                        uuid = UUID.randomUUID().toString(),
                        timestamp = System.currentTimeMillis(),
                        isActive = true,
                        metadata = MessageMetadata(
                            openAiId = "openai-id",
                            finishReason = "finish-reason",
                            promptTokens = 150,
                            completionTokens = 150,
                            totalTokens = 300,
                        ),
                    ),
                )
        }
    }
}