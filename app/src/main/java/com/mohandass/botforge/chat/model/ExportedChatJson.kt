// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model

import com.google.gson.GsonBuilder

data class ExportedChatJson(
    val timestamp: Long = System.currentTimeMillis(),
    val chatInfo: Chat? = null,
    val messageCount: Int,
    val messages: List<Message>
) {

    fun toPrettyJson(): String {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Role::class.java, RoleAdapter())
            .create()

        return gson.toJson(this)
    }
}
