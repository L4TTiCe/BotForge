package com.mohandass.botforge.sync.model.service

import android.util.Log
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.model.dao.entities.BotE

class BotServiceImpl(private val botDao: BotDao) {

    suspend fun searchBots(query: String): List<BotE> {
        return botDao.search("*$query*")
    }

    suspend fun getBots(
        limit: Int = 15,
        offset: Int = 0
    ): List<BotE> {
        return botDao.getBots(limit, offset)
    }

    suspend fun addBot(bot: BotE) {
        botDao.addBot(bot)
        val botFts = bot.toBotFts()
        botDao.addBotFts(botFts)

        Log.v(TAG, "addBot: $botFts")
    }

    suspend fun deleteAllBots() {
        botDao.deleteAllBots()
        botDao.deleteAllBotsFts()
    }

    companion object {
        private const val TAG = "BotServiceImpl"
    }
}