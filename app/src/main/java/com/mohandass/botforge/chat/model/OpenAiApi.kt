package com.mohandass.botforge.chat.model

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import java.util.*

@OptIn(BetaOpenAI::class)
fun ChatMessage.toMessage(): Message {
    return Message(
        text = this.content,
        role = Role.from(this.role),
        uuid = UUID.randomUUID().toString(),
    )
}

@OptIn(BetaOpenAI::class)
fun ChatMessage.from(message: Message): ChatMessage {
    return ChatMessage(
        content = message.text,
        role = message.role.toChatRole(),
    )
}

@OptIn(BetaOpenAI::class)
fun ChatRole.toChatRole(): Role {
    return when (this) {
        ChatRole.User -> Role.USER
        ChatRole.Assistant -> Role.BOT
        ChatRole.System -> Role.SYSTEM
        else -> {
            throw IllegalArgumentException("Unknown role: $this")
        }
    }
}

@OptIn(BetaOpenAI::class)
fun ChatRole.from(role: Role): ChatRole {
    return when (role) {
        Role.USER -> ChatRole.User
        Role.BOT -> ChatRole.Assistant
        Role.SYSTEM -> ChatRole.System
        else -> {
            throw IllegalArgumentException("Unknown role: $role")
        }
    }
}
