package com.mohandass.botforge.common.service

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.model.dao.entities.*

@androidx.room.Database(
    entities = [
        Persona::class,
        ChatE::class,
        MessageE::class,
        MessageMetadataE::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CustomTypeConverters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun personaService(): PersonaDao
    abstract fun chatDao(): ChatDao

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