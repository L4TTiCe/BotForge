// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.repositories

import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.common.services.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * This Class calls the DAO to perform CRUD operations on the Persona table
 */
class PersonaRepository(
    private val personaDao: PersonaDao,
    private val logger: Logger
) {
    private val _personas = MutableStateFlow<List<Persona>>(emptyList())
    val personas: StateFlow<List<Persona>> = _personas

    init {
        // Launch a coroutine to update the personas
        CoroutineScope(Dispatchers.IO).launch {
            updatePersonas()
        }
    }

    private suspend fun updatePersonas() {
        _personas.value = personaDao.getAllPersonas()
    }

    private suspend fun allPersonas(): List<Persona> {
        return personaDao.getAllPersonas()
    }

    suspend fun addPersona(persona: Persona) {
        personaDao.addPersona(persona)

        // emit the new list of personas
        updatePersonas()
    }

    suspend fun getPersona(uuid: String): Persona {
        return personaDao.getPersona(uuid)
    }

    suspend fun updatePersona(persona: Persona) {
        personaDao.updatePersona(persona)
        updatePersonas()
    }

    suspend fun deletePersona(persona: Persona) {
        personaDao.deletePersona(persona)
        updatePersonas()
    }

    suspend fun deleteAllPersonas() {
        personaDao.deleteAllPersonas()
        updatePersonas()
    }

    companion object {
        private const val TAG = "PersonaRepository"
    }
}
