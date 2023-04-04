package com.mohandass.botforge.sync.service

/**
 * A service to perform operations on the the Firestore Database
 *
 * Supports operations on the UpVotes, DownVotes and Reports collections
 */
interface FirestoreService {
    suspend fun addUpVote(botId: String, userId: String)
    suspend fun addDownVote(botId: String, userId: String)

    suspend fun checkUpVote(botId: String, userId: String): Boolean
    suspend fun checkDownVote(botId: String, userId: String): Boolean
    suspend fun addReport(botId: String, userId: String)
    suspend fun checkReport(botId: String, userId: String): Boolean


    suspend fun getUpVotes(botId: String): Long
    suspend fun getDownVotes(botId: String): Long
}