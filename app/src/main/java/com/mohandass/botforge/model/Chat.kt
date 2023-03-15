package com.mohandass.botforge.model

import java.util.*

data class Chat(
    val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val personaUuid: String = "",
    val savedAt: Long = System.currentTimeMillis(),

    val messages: List<Message> = emptyList(),
)
