package com.mohandass.botforge.model.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mohandass.botforge.model.entities.Persona

@Dao
interface PersonaService {

    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun addPersona(persona: Persona)

    @Update
    suspend fun updatePersona(persona: Persona)

    @Query("SELECT * FROM persona ORDER BY lastUsed DESC")
    suspend fun getAllPersonas(): List<Persona>
}