package com.mohandass.botforge.chat.model

import com.mohandass.botforge.chat.model.dao.entities.MessageMetadataE

/**
 * A data class to represent a Message's Metadata
 */
data class MessageMetadata(
    val openAiId: String? = null,
    val finishReason: String? = null,
    val promptTokens: Int? = null,
    val completionTokens: Int? = null,
    val totalTokens: Int? = null,
) {
    override fun toString(): String {
        return "MessageMetadata(" +
                "openAiId=$openAiId," +
                "finishReason=$finishReason," +
                "promptTokens=$promptTokens," +
                "completionTokens=$completionTokens," +
                "totalTokens=$totalTokens" +
                ")"
    }

    companion object {
        fun from(messageMetadata: MessageMetadataE): MessageMetadata {
            return MessageMetadata(
                openAiId = messageMetadata.openAiId,
                finishReason = messageMetadata.finishReason,
                promptTokens = messageMetadata.promptTokens,
                completionTokens = messageMetadata.completionTokens,
                totalTokens = messageMetadata.totalTokens,
            )
        }
    }
}
