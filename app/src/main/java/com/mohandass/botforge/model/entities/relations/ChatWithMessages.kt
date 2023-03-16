package com.mohandass.botforge.model.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.model.entities.ChatE
import com.mohandass.botforge.model.entities.MessageE

data class ChatWithMessages(
    @Embedded val chat: ChatE,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "chatUuid",
        entity = MessageE::class
    )
    val messages: List<MessageE>
)
