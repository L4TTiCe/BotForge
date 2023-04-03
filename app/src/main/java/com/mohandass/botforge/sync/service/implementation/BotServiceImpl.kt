package com.mohandass.botforge.sync.service.implementation

import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.service.BotService

class BotServiceImpl(
    private val botDao: BotDao,
    private val logger: Logger
) : BotService {
    override suspend fun addBot(bot: BotE) {
        botDao.addBot(bot)
        val botFts = bot.toBotFts()
        if (botDao.botFtsExists(bot.uuid)) {
            botDao.updateBotFts(
                bot.uuid,
                botFts.name,
                botFts.alias,
                botFts.systemMessage,
                botFts.description,
                botFts.tags,
                botFts.createdBy
            )
        } else {
            botDao.addBotFts(botFts)
        }

        logger.logVerbose(TAG, "addBot: $botFts")
    }

    override suspend fun getBot(uuid: String): BotE? {
        return botDao.getBot(uuid)
    }

    override suspend fun searchBots(query: String): List<BotE> {
        return botDao.search("*$query*")
    }

    override suspend fun getBots(
        limit: Int,
        offset: Int
    ): List<BotE> {
        return botDao.getBots(limit, offset)
    }

    override suspend fun deleteBot(uuid: String) {
        botDao.deleteBot(uuid)
        botDao.deleteBotFts(uuid)
    }

    override suspend fun deleteAllBots() {
        botDao.deleteAllBots()
        botDao.deleteAllBotsFts()
    }

    companion object {
        private const val TAG = "BotServiceImpl"
    }
}