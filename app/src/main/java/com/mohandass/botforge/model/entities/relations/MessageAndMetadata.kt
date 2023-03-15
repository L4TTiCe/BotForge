package com.mohandass.botforge.model.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.model.entities.MessageE
import com.mohandass.botforge.model.entities.MessageMetadataE

data class MessageAndMetadata (
    @Embedded val message: MessageE,

    @Relation(
        parentColumn = "metadataOpenAiId",
        entityColumn = "openAiId",
        entity = MessageMetadataE::class
    )
    val metadata: MessageMetadataE
)
