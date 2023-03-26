package com.mohandass.botforge.sync.model.service.implementation

import android.util.Log
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.service.BotService

class BotServiceImpl(private val botDao: BotDao): BotService {

    override suspend fun searchBots(query: String): List<BotE> {
        return botDao.search("*$query*")
    }

    override suspend fun getBots(
        limit: Int,
        offset: Int
    ): List<BotE> {
        return botDao.getBots(limit, offset)
    }

    override suspend fun addBot(bot: BotE) {
        botDao.addBot(bot)
        val botFts = bot.toBotFts()
        botDao.addBotFts(botFts)

        Log.v(TAG, "addBot: $botFts")
    }

    override suspend fun deleteAllBots() {
        botDao.deleteAllBots()
        botDao.deleteAllBotsFts()
    }

    companion object {
        private const val TAG = "BotServiceImpl"
    }
}