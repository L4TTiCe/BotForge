package com.mohandass.botforge.model.entities

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import java.util.*

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
            onDelete = CASCADE
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
