// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.service.implementation

import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.service.BotService

/**
 * A service to handle operations on the BotE entity.
 */
class BotServiceImpl(
    private val botDao: BotDao,
    private val logger: Logger
) : BotService {

    // Given a BotE, add it to BotE table and BotFts table
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

    override suspend fun getMostRecentBots(limit: Int, offset: Int): List<BotE> {
        return botDao.getMostRecentBots(limit, offset)
    }

    override suspend fun getRandomBots(limit: Int): List<BotE> {
        return botDao.getRandomBots(limit)
    }

    // Get bots that match the query
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