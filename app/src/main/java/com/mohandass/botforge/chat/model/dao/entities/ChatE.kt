// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model.dao.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohandass.botforge.chat.model.Chat
import java.util.*

/**
 * A data class to represent a Chat
 *
 * This class is used to represent a Saved Chat in the Database
 */
@Entity(tableName = "chats")
data class ChatE(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val personaUuid: String? = null,
    val savedAt: Long = System.currentTimeMillis(),
) {
    override fun toString(): String {
        return "ChatE(" +
                "uuid='$uuid'," +
                "name='$name', " +
                "personaUuid='$personaUuid', " +
                "savedAt=$savedAt" +
                ")"
    }

    companion object {
        fun from(chat: Chat): ChatE {
            return ChatE(
                name = chat.name,
                personaUuid = chat.personaUuid,
                savedAt = chat.savedAt,
            )
        }
    }
}
