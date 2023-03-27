package com.mohandass.botforge.sync.model

data class VoteRecord(
    val botId: String,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)
