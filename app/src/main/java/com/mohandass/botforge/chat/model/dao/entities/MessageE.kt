package com.mohandass.botforge.chat.model.dao.entities

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.Role
import java.util.*

/**
 * A data class to represent a Message
 *
 * This class is used to represent a Saved Message in the Database
 */
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatE::class,
            parentColumns = ["uuid"],
            childColumns = ["chatUuid"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = MessageMetadataE::class,
            parentColumns = ["openAiId"],
            childColumns = ["metadataOpenAiId"],
            onDelete = ForeignKey.SET_NULL
        )],
    indices = [Index("chatUuid"), Index("metadataOpenAiId")]
)
@TypeConverters(CustomTypeConverters::class)
data class MessageE(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val text: String = "",
    val role: Role = Role.USER,
    val timestamp: Long = System.currentTimeMillis(),
    var metadataOpenAiId: String? = null,
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
        // This function is used to convert a Message to a MessageE
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
