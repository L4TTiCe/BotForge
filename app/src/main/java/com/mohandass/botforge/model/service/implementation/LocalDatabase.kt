package com.mohandass.botforge.model.service.implementation

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohandass.botforge.model.service.PersonaService

@androidx.room.Database(
    entities = [com.mohandass.botforge.model.entities.Persona::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun personaService(): PersonaService

    companion object {
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
                    "botforge_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}