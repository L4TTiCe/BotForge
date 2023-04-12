// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.dao.entities.BotFts

/**
 * A DAO to interact with the [BotE] and [BotFts] entities
 */
@Dao
interface BotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBot(bot: BotE)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBotFts(bot: BotFts)

    @Query("SELECT EXISTS(SELECT 1 FROM bots_fts WHERE uuid = :uuid)")
    suspend fun botFtsExists(uuid: String): Boolean

    @Query("SELECT * FROM bots WHERE uuid = :uuid")
    suspend fun getBot(uuid: String): BotE?

    // Update BotFts with uuid
    @Query(
        """
            UPDATE bots_fts
            SET name = :name, alias = :alias, systemMessage = :systemMessage, description = :description, tags = :tags, createdBy = :createdBy
            WHERE uuid = :uuid
        """
    )
    suspend fun updateBotFts(
        uuid: String,
        name: String,
        alias: String,
        systemMessage: String,
        description: String,
        tags: String,
        createdBy: String
    )

    @Query(
        """
      SELECT bots.*
      FROM bots
      JOIN bots_fts ON bots.uuid = bots_fts.uuid
      WHERE bots_fts MATCH :query
      ORDER BY (bots.userUpVotes + bots.usersCount ) DESC
    """
    )
    suspend fun search(
        query: String,
//        limit: Int = 10,
//        offset: Int = 0
    ): List<BotE>

    @Query(
        """
       SELECT * FROM bots 
       ORDER BY (bots.userUpVotes + bots.usersCount ) DESC 
       LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getBots(
        limit: Int = 15,
        offset: Int = 0
    ): List<BotE>

    @Query(
        """
        SELECT * FROM bots 
        ORDER BY bots.createdAt DESC 
        LIMIT :limit OFFSET :offset
        """
    )
    suspend fun getMostRecentBots(
        limit: Int = 15,
        offset: Int = 0
    )
    : List<BotE>

    @Query(
        """
        SELECT * FROM bots 
        ORDER BY RANDOM()
        LIMIT :limit
        """
    )
    suspend fun getRandomBots(
        limit: Int = 15,
    ): List<BotE>

    // Delete bot with uuid
    @Query("DELETE FROM bots WHERE uuid = :uuid")
    suspend fun deleteBot(uuid: String)

    @Query("DELETE FROM bots_fts WHERE uuid = :uuid")
    suspend fun deleteBotFts(uuid: String)

    // Delete all bots
    @Query("DELETE FROM bots")
    suspend fun deleteAllBots()

    @Query("DELETE FROM bots_fts")
    suspend fun deleteAllBotsFts()
}
