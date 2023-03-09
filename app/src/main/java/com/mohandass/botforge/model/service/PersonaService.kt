package com.mohandass.botforge.model.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mohandass.botforge.model.entities.Persona

@Dao
interface PersonaService {

    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun addPersona(persona: Persona)

    @Query("SELECT * FROM persona ORDER BY lastUsed DESC")
    suspend fun getAllPersonas(): List<Persona>
}