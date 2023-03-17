package com.mohandass.botforge.model.service.implementation

import com.mohandass.botforge.model.entities.Persona
import com.mohandass.botforge.model.dao.PersonaDao

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