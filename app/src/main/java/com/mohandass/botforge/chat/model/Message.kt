package com.mohandass.botforge.chat.model

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatMessage
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import java.util.*

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
            return Message(
                text = message.content,
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
    }
}
