package com.mohandass.botforge.chat.model.dao

import androidx.room.*
import com.mohandass.botforge.chat.model.dao.entities.Persona

/**
 * A DAO to interact with the [Persona] entity
 */
@Dao
interface PersonaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPersona(persona: Persona)

    @Query("SELECT * FROM persona ORDER BY lastUsed DESC")
    suspend fun getAllPersonas(): List<Persona>

    @Update
    suspend fun updatePersona(persona: Persona)

    @Delete
    suspend fun deletePersona(persona: Persona)

    @Query("DELETE FROM persona")
    suspend fun deleteAllPersonas()
}
