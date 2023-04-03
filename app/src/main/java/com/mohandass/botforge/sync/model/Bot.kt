package com.mohandass.botforge.sync.model

import com.google.firebase.database.IgnoreExtraProperties
import com.mohandass.botforge.sync.model.dao.entities.BotE

@IgnoreExtraProperties
data class Bot(
    val uuid: String? = null,
    val parentUuid: String? = null,
    val name: String? = null,
    var alias: String? = null,
    val systemMessage: String? = null,
    val createdAt: Long = System.currentTimeMillis(),

    val description: String? = null,
    val tags: String? = null,

    val usersCount: Int? = null,
    val userUpVotes: Int? = null,
    val userDownVotes: Int? = null,

    val updatedAt: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
) {
    fun toBotE(): BotE {
        return BotE(
            uuid = uuid!!,
            parentUuid = parentUuid!!,
            name = name!!,
            alias = alias!!,
            systemMessage = systemMessage!!,
            createdAt = createdAt,

            description = description!!,
            tags = tags!!.split(","),

            usersCount = usersCount!!,
            userUpVotes = userUpVotes!!,
            userDownVotes = userDownVotes!!,

            updatedAt = updatedAt,
            createdBy = createdBy!!,
        )
    }
}
