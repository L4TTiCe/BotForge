package com.mohandass.botforge.sync.model.service.implementation

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.sync.model.Bot
import kotlinx.coroutines.tasks.await

class FirebaseDatabaseServiceImpl(
    private val logger: Logger
) {
    private val database = Firebase.database
    private val botRef = database.getReference(BOT_COLLECTION)

    suspend fun writeNewBot(bot: Bot) {
        botRef.child(bot.uuid!!).setValue(bot).await()
    }

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

    companion object {
        const val TAG = "FirebaseDatabaseServiceImpl"
        const val BOT_COLLECTION = "bots"
    }
}