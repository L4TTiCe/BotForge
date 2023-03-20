package com.mohandass.botforge.chat.model.dao.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.chat.model.dao.entities.ChatE
import com.mohandass.botforge.chat.model.dao.entities.MessageE

data class ChatWithMessages(
    @Embedded val chat: ChatE,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "chatUuid",
        entity = MessageE::class
    )
    val messages: List<MessageE>
)
