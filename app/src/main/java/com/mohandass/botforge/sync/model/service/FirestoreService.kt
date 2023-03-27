package com.mohandass.botforge.sync.model.service

interface FirestoreService {
    suspend fun addUpVote(botId: String, userId: String)
    suspend fun addDownVote(botId: String, userId: String)

    suspend fun checkUpVote(botId: String, userId: String): Boolean
    suspend fun checkDownVote(botId: String, userId: String): Boolean

    suspend fun getUpVotes(botId: String): Long
    suspend fun getDownVotes(botId: String): Long
}