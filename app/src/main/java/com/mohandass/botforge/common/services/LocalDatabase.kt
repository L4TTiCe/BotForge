// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.model.dao.entities.ChatE
import com.mohandass.botforge.chat.model.dao.entities.CustomTypeConvertersChat
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import com.mohandass.botforge.chat.model.dao.entities.MessageMetadataE
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.image.model.dao.ImageGenerationDao
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE
import com.mohandass.botforge.sync.model.dao.BotDao
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.dao.entities.BotFts
import com.mohandass.botforge.sync.model.dao.entities.CustomSyncTypeConverters

/**
 * A singleton database to store all the data locally in a Room database
 */
@androidx.room.Database(
    entities = [
        // Personas
        Persona::class,

        // Chats
        ChatE::class,
        MessageE::class,
        MessageMetadataE::class,

        // Bots
        BotE::class,
        BotFts::class,

        // Generated Images
        GeneratedImageE::class,
        ImageGenerationRequestE::class,
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ],
    exportSchema = true
)
@TypeConverters(CustomTypeConvertersChat::class, CustomSyncTypeConverters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun personaService(): PersonaDao
    abstract fun chatDao(): ChatDao
    abstract fun botDao(): BotDao
    abstract fun imageDao(): ImageGenerationDao

    companion object {
        private const val DB_NAME = "botforge_db"

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(applicationContext: Context): LocalDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext,
                    LocalDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
