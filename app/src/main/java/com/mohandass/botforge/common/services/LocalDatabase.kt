package com.mohandass.botforge.common.services

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.model.dao.entities.*
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
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
@TypeConverters(CustomTypeConverters::class, CustomSyncTypeConverters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun personaService(): PersonaDao
    abstract fun chatDao(): ChatDao
    abstract fun botDao(): BotDao

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