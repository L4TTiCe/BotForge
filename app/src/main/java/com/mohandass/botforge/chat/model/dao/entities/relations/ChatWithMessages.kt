package com.mohandass.botforge.chat.model.dao.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.chat.model.dao.entities.ChatE
import com.mohandass.botforge.chat.model.dao.entities.MessageE

/**
 * A data class to represent a Chat with its Messages
 *
 * This relation is used to get a reference to the Chat and its Messages
 */
data class ChatWithMessages(
    @Embedded val chat: ChatE,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "chatUuid",
        entity = MessageE::class
    )
    val messages: List<MessageE>
)
