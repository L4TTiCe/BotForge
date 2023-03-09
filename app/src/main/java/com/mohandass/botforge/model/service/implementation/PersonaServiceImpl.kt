package com.mohandass.botforge.model.service.implementation

import com.mohandass.botforge.model.entities.Persona
import com.mohandass.botforge.model.service.PersonaService

class PersonaServiceImpl(private val PersonaService: PersonaService)  {

    suspend fun allPersonas(): List<Persona> {
        return PersonaService.getAllPersonas()
    }

    suspend fun addPersona(persona: Persona) {
        PersonaService.addPersona(persona)
    }
}