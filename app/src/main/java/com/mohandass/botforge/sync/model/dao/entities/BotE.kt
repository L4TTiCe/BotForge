package com.mohandass.botforge.sync.model.dao.entities

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohandass.botforge.chat.model.dao.entities.Persona
import java.util.*

@Entity(tableName = "bots")
data class BotE(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val name: String,
    var alias: String,
    val systemMessage: String,
    val createdAt: Long = System.currentTimeMillis(),

    val description: String,
    val tags: List<String>,

    val usersCount: Int,
    val userUpVotes: Int,
    val userDownVotes: Int,

    val updatedAt: Long,
    val createdBy: String,
) {

    fun toBotFts(): BotFts {
        return BotFts(
            uuid = uuid,
            name = name.lowercase(),
            alias = alias.lowercase(),
            systemMessage = systemMessage.lowercase(),
            description = description.lowercase(),
            tags = tags.joinToString(",").lowercase(),
            createdBy = createdBy.lowercase()
        )
    }

    fun toPersona(): Persona {
        return Persona(
            uuid = UUID.randomUUID().toString(),
            name = name,
            alias = alias,
            systemMessage = systemMessage,
            createdAt = createdAt,
            lastUsed = Date().time,
        )
    }

    override fun toString(): String {
        return "BotE(" +
                "uuid='$uuid'," +
                "alias='$alias', " +
                "name='$name', " +
                "systemMessage='$systemMessage', " +
                "createdAt=$createdAt, " +
                "description='$description', " +
                "tags=$tags, " +
                "usersCount=$usersCount, " +
                "userUpVotes=$userUpVotes, " +
                "userDownVotes=$userDownVotes, " +
                "updatedAt=$updatedAt, " +
                "createdBy='$createdBy'" +
                ")"
    }
}

class BotEProvider : PreviewParameterProvider<BotE> {
    private val bot1 = BotE(
        uuid = UUID.randomUUID().toString(),
        name = "Personal Assistant",
        alias = "\uD83D\uDC80",
        systemMessage = "You are Chat GPT",
        description = "generic chatbot",
        tags = listOf("tag1", "TAG2"),
        createdBy = "admin",
        usersCount = 0,
        userUpVotes = 0,
        userDownVotes = 0,
        createdAt = Date().time,
        updatedAt = Date().time,
    )

    private val bot2 = BotE(
        uuid = UUID.randomUUID().toString(),
        name = "HealthGPT",
        alias = "\uD83D\uDD25",
        systemMessage = "You are HealthGPT-2.",
        description = "Your personal bot to deduce your health and fitness",
        tags = listOf("Healthcare", "Education"),
        createdBy = "admin",
        usersCount = 0,
        userUpVotes = 0,
        userDownVotes = 0,
        createdAt = Date().time,
        updatedAt = Date().time,
    )
    override val values = listOf(bot1, bot2).asSequence()
}
