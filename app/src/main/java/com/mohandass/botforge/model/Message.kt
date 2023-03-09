package com.mohandass.botforge.model

import java.util.*

data class Message(
    val text: String = "",
    val role: Role = Role.USER,
    val uuid: String = UUID.randomUUID().toString(),
) {
    override fun toString(): String {
        return "Message(text='$text', role=$role, uuid='$uuid')"
    }
}
