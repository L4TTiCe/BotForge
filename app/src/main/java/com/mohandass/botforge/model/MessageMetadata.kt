package com.mohandass.botforge.model

data class MessageMetadata (
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
}
