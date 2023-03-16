package com.mohandass.botforge.model

import java.util.*

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
