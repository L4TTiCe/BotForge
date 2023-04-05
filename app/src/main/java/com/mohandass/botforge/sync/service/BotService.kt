// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.service

import com.mohandass.botforge.sync.model.dao.entities.BotE

/**
 * A service to perform operations on the BotE entity
 */
interface BotService {
    suspend fun addBot(bot: BotE)

    suspend fun getBot(uuid: String): BotE?

    suspend fun searchBots(query: String): List<BotE>

    suspend fun getBots(
        limit: Int = 15,
        offset: Int = 0
    ): List<BotE>

    suspend fun deleteBot(uuid: String)

    suspend fun deleteAllBots()
}
