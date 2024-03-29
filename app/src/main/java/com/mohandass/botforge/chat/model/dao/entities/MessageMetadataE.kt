// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model.dao.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohandass.botforge.chat.model.MessageMetadata

/**
 * A data class to represent a Message Metadata
 *
 * This class is used to represent a Saved Message Metadata in the Database
 */
@Entity
data class MessageMetadataE(
    @PrimaryKey val openAiId: String = "",
    val finishReason: String? = null,
    val promptTokens: Int? = null,
    val completionTokens: Int? = null,
    val totalTokens: Int? = null,
    val timestamp: Long = System.currentTimeMillis(),
) {
    override fun toString(): String {
        return "MessageMetadata(" +
                "openAiId='$openAiId'," +
                "finishReason='$finishReason', " +
                "promptTokens=$promptTokens, " +
                "completionTokens=$completionTokens, " +
                "totalTokens=$totalTokens, " +
                "timestamp=$timestamp" +
                ")"
    }

    companion object {
        // This function is used to convert a MessageMetadata to a MessageMetadataE
        fun from(messageMetadata: MessageMetadata): MessageMetadataE? {
            return messageMetadata.openAiId?.let {
                MessageMetadataE(
                    openAiId = it,
                    finishReason = messageMetadata.finishReason,
                    promptTokens = messageMetadata.promptTokens,
                    completionTokens = messageMetadata.completionTokens,
                    totalTokens = messageMetadata.totalTokens,
                )
            }
        }
    }
}
