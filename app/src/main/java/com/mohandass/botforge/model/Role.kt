package com.mohandass.botforge.model

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatRole

class Role(val ordinal: Int) {

    companion object {
        val USER = Role(0)
        val BOT = Role(1)
        val SYSTEM = Role(2)

        private val values = mapOf(
            "USER" to USER,
            "BOT" to BOT,
            "SYSTEM" to SYSTEM,
        )

        fun valueOf(name: String): Role? {
            return values[name]
        }

        fun values(): List<Role> {
            return values.values.toList()
        }

        @OptIn(BetaOpenAI::class)
        fun from(charRole: ChatRole): Role {
            return when (charRole) {
                ChatRole.User -> USER
                ChatRole.Assistant -> BOT
                ChatRole.System -> SYSTEM
                else -> {
                    throw IllegalArgumentException("Unknown role: $charRole")
                }
            }
        }
    }

    @OptIn(BetaOpenAI::class)
    fun toChatRole(): ChatRole {
        return when (this) {
            USER -> ChatRole.User
            BOT -> ChatRole.Assistant
            SYSTEM -> ChatRole.System
            else -> {
                throw IllegalArgumentException("Unknown role: $this")
            }
        }
    }

    fun isUser() = this == USER

    override fun equals(other: Any?): Boolean {
        return other is Role && this.ordinal == other.ordinal
    }

    override fun hashCode(): Int {
        return ordinal.hashCode()
    }
}
