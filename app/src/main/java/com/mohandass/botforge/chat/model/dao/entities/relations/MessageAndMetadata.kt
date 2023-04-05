// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model.dao.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import com.mohandass.botforge.chat.model.dao.entities.MessageMetadataE

/**
 * A data class to represent a Message with its Metadata
 *
 * This relation is used to get a reference to the Message and its Metadata
 */
data class MessageAndMetadata(
    @Embedded val message: MessageE,

    @Relation(
        parentColumn = "metadataOpenAiId",
        entityColumn = "openAiId",
        entity = MessageMetadataE::class
    )
    val metadata: MessageMetadataE
)
