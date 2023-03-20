package com.mohandass.botforge.chat.model.dao.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import com.mohandass.botforge.chat.model.dao.entities.MessageMetadataE

data class MessageAndMetadata (
    @Embedded val message: MessageE,

    @Relation(
        parentColumn = "metadataOpenAiId",
        entityColumn = "openAiId",
        entity = MessageMetadataE::class
    )
    val metadata: MessageMetadataE
)
