package com.mohandass.botforge.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import java.util.*

@Entity(tableName = "messages")
@TypeConverters(CustomTypeConverters::class)
data class MessageE(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val text: String = "",
    val role: Role = Role.USER,
    val timestamp: Long = System.currentTimeMillis(),
    val metadataOpenAiId: String = "",
    var chatUuid: String = "",
) {
    override fun toString(): String {
        return "MessageE(" +
                "uuid='$uuid'," +
                "text='$text', " +
                "role=$role, " +
                "timestamp=$timestamp, " +
                "metadataOpenAiId=$metadataOpenAiId, " +
                "chatUuid=$chatUuid" +
                ")"
    }

    companion object {
        fun from(message: Message): MessageE {
            return MessageE(
                text = message.text,
                role = message.role,
                timestamp = message.timestamp,
                metadataOpenAiId = (if (message.metadata?.openAiId != null) message.metadata!!.openAiId else "").toString(),
            )
        }
    }
}
