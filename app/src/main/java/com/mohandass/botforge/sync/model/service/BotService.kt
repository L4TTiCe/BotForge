package com.mohandass.botforge.sync.model.service

import com.mohandass.botforge.sync.model.dao.entities.BotE

interface BotService {
    suspend fun addBot(bot: BotE)

    suspend fun searchBots(query: String): List<BotE>

    suspend fun getBots(
        limit: Int = 15,
        offset: Int = 0
    ): List<BotE>

    suspend fun deleteAllBots()
}