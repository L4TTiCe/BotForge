package com.mohandass.botforge.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohandass.botforge.model.MessageMetadata

@Entity
data class MessageMetadataE (
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
