package com.mohandass.botforge.model

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatMessage
import java.util.*

data class Message(
    val text: String = "",
    val role: Role = Role.USER,
    val uuid: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: MessageMetadata? = null,
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
    }
}
