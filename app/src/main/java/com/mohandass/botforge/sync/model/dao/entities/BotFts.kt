package com.mohandass.botforge.sync.model.dao.entities

import androidx.room.Entity
import androidx.room.Fts4

/**
 * A subset of [BotE] to be used for Full Text Search in the Database
 * @see BotE
 *
 * Contains only the fields that are to be searched.
 */
@Fts4
@Entity(tableName = "bots_fts")
data class BotFts(
    val uuid: String,
    val name: String,
    var alias: String,
    val systemMessage: String,

    val description: String,
    val tags: String,

    val createdBy: String,
)