package com.mohandass.botforge.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohandass.botforge.model.Chat
import java.util.*

@Entity(tableName = "chats")
data class ChatE(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val personaUuid: String = "",
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
                name= chat.name,
                personaUuid = chat.personaUuid,
                savedAt = chat.savedAt,
            )
        }
    }
}
