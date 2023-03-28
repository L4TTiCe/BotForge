package com.mohandass.botforge.sync.model.service.implementation

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.sync.model.VoteRecord
import com.mohandass.botforge.sync.model.service.FirestoreService
import kotlinx.coroutines.tasks.await

class FirestoreServiceImpl(
    private val logger: Logger
) : FirestoreService {
    private val db = Firebase.firestore
    private val upVoteRef = db.collection(UP_VOTES_COLLECTION)
    private val downVoteRef = db.collection(DOWN_VOTES_COLLECTION)

    private suspend fun recordUpVote(botId: String, userId: String) {
        logger.logVerbose(TAG, "recordUpVote: $botId, user: $userId")
        val upVoteRecord = VoteRecord(botId, userId)
        upVoteRef.add(upVoteRecord).await()
    }

    private suspend fun deleteUpVote(botId: String, userId: String) {
        logger.logVerbose(TAG, "deleteUpVote: $botId, user: $userId")
        val querySnapshot = upVoteRef.whereEqualTo("botId", botId)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].reference.delete().await()
        }
    }

    private suspend fun recordDownVote(botId: String, userId: String) {
        logger.logVerbose(TAG, "recordDownVote: $botId, user: $userId")
        val downVoteRecord = VoteRecord(botId, userId)
        downVoteRef.add(downVoteRecord).await()
    }

    private suspend fun deleteDownVote(botId: String, userId: String) {
        logger.logVerbose(TAG, "deleteDownVote: $botId, user: $userId")
        val querySnapshot = downVoteRef.whereEqualTo("botId", botId)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].reference.delete().await()
        }
    }

    override suspend fun checkUpVote(botId: String, userId: String): Boolean {
        logger.logVerbose(TAG, "checkUpVote: $botId, user: $userId")
        return try {
            val querySnapshot = upVoteRef.whereEqualTo("botId", botId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            logger.logError(TAG, "Error checking UpVote", e)
            false
        }
    }

    override suspend fun checkDownVote(botId: String, userId: String): Boolean {
        logger.logVerbose(TAG, "checkDownVote: $botId, user: $userId")
        return try {
            val querySnapshot = downVoteRef.whereEqualTo("botId", botId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            logger.logError(TAG, "Error checking DownVote", e)
            false
        }
    }

    override suspend fun addUpVote(botId: String, userId: String) {
        logger.logVerbose(TAG, "addUpVote: $botId, user: $userId")
        try {
            deleteUpVote(botId, userId)
            deleteDownVote(botId, userId)
            recordUpVote(botId, userId)
        } catch (e: Exception) {
            logger.logError(TAG, "Error adding UpVote", e)
        }
    }

    override suspend fun addDownVote(botId: String, userId: String) {
        logger.logVerbose(TAG, "addDownVote: $botId, user: $userId")
        try {
            deleteDownVote(botId, userId)
            deleteUpVote(botId, userId)
            recordDownVote(botId, userId)
        } catch (e: Exception) {
            logger.logError(TAG, "Error adding DownVote", e)
        }
    }

    override suspend fun getUpVotes(botId: String): Long {
        return try {
            val query = upVoteRef.whereEqualTo("botId", botId)
            val countQuery = query.count()
            val snapshot = countQuery.get(AggregateSource.SERVER).await()
            snapshot.count
        } catch (e: Exception) {
            logger.logError(TAG, "Error getting UpVotes", e)
            0
        }
    }

    override suspend fun getDownVotes(botId: String): Long {
        return try {
            val query = downVoteRef.whereEqualTo("botId", botId)
            val countQuery = query.count()
            val snapshot = countQuery.get(AggregateSource.SERVER).await()
            snapshot.count
        } catch (e: Exception) {
            logger.logError(TAG, "Error getting DownVotes", e)
            0
        }
    }

    companion object {
        const val TAG = "FirestoreServiceImpl"
        const val UP_VOTES_COLLECTION = "up_votes"
        const val DOWN_VOTES_COLLECTION = "down_votes"
    }
}