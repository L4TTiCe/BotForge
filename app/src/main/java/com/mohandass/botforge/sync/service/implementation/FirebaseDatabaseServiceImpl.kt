// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.service.implementation

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.sync.model.Bot
import com.mohandass.botforge.sync.model.DeleteRecord
import kotlinx.coroutines.tasks.await

/**
 * A service to sync data with the Firebase Realtime Database
 */
class FirebaseDatabaseServiceImpl(
    private val logger: Logger
) {
    private val database = Firebase.database
    private val botRef = database.getReference(BOT_COLLECTION)
    private val deleteRef = database.getReference(DELETE_COLLECTION)

    // Write a new Bot to the Database
    suspend fun writeNewBot(bot: Bot) {
        botRef.child(bot.uuid!!).setValue(bot).await()
    }

    // Fetch all bots from the Database, that were updated after the given time
    // This is used to sync the local database with the remote database
    suspend fun fetchBotsUpdatedAfter(time: Long): List<Bot> {
        val bots = mutableListOf<Bot>()
        val snapshot = botRef.orderByChild("updatedAt").startAt(time.toDouble()).get().await()
        logger.logVerbose(TAG, "fetchBotsUpdatedAfter: ${snapshot.childrenCount}")
        snapshot.children.forEach {
            logger.logVerbose(TAG, "fetchBotsUpdatedAfter: ${it.getValue(Bot::class.java)}")
            bots.add(it.getValue(Bot::class.java)!!)
        }
        return bots
    }

    // Content Moderation
    // Fetch all Bots marked for deletion from the Database, that were added after the given index
    suspend fun fetchBotsDeletedAfter(index: Int): List<DeleteRecord> {
        val records = mutableListOf<DeleteRecord>()
        val snapshot = deleteRef.orderByChild("index").startAt(index.toDouble()).get().await()
        logger.logVerbose(TAG, "fetchBotsDeletedAfter: ${snapshot.childrenCount}")
        snapshot.children.forEach {
            logger.logVerbose(
                TAG, "fetchBotsDeletedAfter: " +
                        "${it.getValue(DeleteRecord::class.java)}"
            )
            records.add(it.getValue(DeleteRecord::class.java)!!)
        }
        return records
    }

    companion object {
        const val TAG = "FirebaseDatabaseServiceImpl"
        const val BOT_COLLECTION = "bots"
        const val DELETE_COLLECTION = "deletesRecord"
    }
}