package com.mohandass.botforge.chat.model

import java.util.*

/**
 * A data class to represent a Chat
 *
 * This class is used to represent a Chat
 */
data class Chat(
    val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val personaUuid: String? = null,
    val savedAt: Long = System.currentTimeMillis(),
) {
    override fun toString(): String {
        return "Chat(" +
                "uuid='$uuid'," +
                "name='$name'," +
                "personaUuid='$personaUuid'," +
                "savedAt=$savedAt," +
                ")"
    }
}
