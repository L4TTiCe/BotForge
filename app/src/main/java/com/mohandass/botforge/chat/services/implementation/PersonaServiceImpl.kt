package com.mohandass.botforge.chat.services.implementation

import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.model.dao.entities.Persona

class PersonaServiceImpl(private val personaDao: PersonaDao) {

    suspend fun allPersonas(): List<Persona> {
        return personaDao.getAllPersonas()
    }

    suspend fun addPersona(persona: Persona) {
        personaDao.addPersona(persona)
    }

    suspend fun updatePersona(persona: Persona) {
        personaDao.updatePersona(persona)
    }

    suspend fun deletePersona(persona: Persona) {
        personaDao.deletePersona(persona)
    }

    suspend fun deleteAllPersonas() {
        personaDao.deleteAllPersonas()
    }
}