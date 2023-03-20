package com.mohandass.botforge.chat.model.services.implementation

import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.model.dao.PersonaDao

class PersonaServiceImpl(private val PersonaDao: PersonaDao)  {

    suspend fun allPersonas(): List<Persona> {
        return PersonaDao.getAllPersonas()
    }

    suspend fun addPersona(persona: Persona) {
        PersonaDao.addPersona(persona)
    }

    suspend fun updatePersona(persona: Persona) {
        PersonaDao.updatePersona(persona)
    }

    suspend fun deletePersona(persona: Persona) {
        PersonaDao.deletePersona(persona)
    }

    suspend fun deleteAllPersonas() {
        PersonaDao.deleteAllPersonas()
    }
}