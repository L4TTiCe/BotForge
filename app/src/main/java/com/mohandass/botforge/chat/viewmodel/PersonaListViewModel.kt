// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.repositories.PersonaRepository
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.service.BotService
import kotlinx.coroutines.launch

/*
 * A ViewModel to handle the Persona List Screen
 */
class PersonaListViewModel (
    private val viewModel: AppViewModel,
    personaRepository: PersonaRepository,
    private val botService: BotService,
    private val logger: Logger
): ViewModel() {
    private var _personas = mutableStateListOf<Persona>()
    val personas = personaRepository.personas.asLiveData()
    // Reference:
    // https://stackoverflow.com/questions/48396092/should-i-include-lifecycleowner-in-viewmodel
    private val observer: (List<Persona>) -> Unit = {
        _personas.clear()
        _personas.addAll(it)
    }

    init {
        personas.observeForever(observer)
    }

    override fun onCleared() {
        personas.removeObserver(observer)
        super.onCleared()
    }
    var matchedPersonas = mutableStateListOf<Persona>()

    val showDeleteAllPersonaDialog = mutableStateOf(false)
    val searchQuery = mutableStateOf("")

    private val bots = mutableMapOf<String, BotE?>()

    fun updateSearchQuery(query: String) {
        logger.logVerbose(TAG, "onSearchQueryChange $query")
        searchQuery.value = query
        filterPersonas()
    }

    private fun filterPersonas() {
        logger.logVerbose(TAG, "filterPersonas")
        matchedPersonas.clear()

        if (searchQuery.value.isEmpty() || searchQuery.value.isBlank()) {
            return
        }
        personas.value!!.filterTo(matchedPersonas) { persona ->

            persona.name.contains(searchQuery.value, ignoreCase = true) ||
                    persona.alias.contains(searchQuery.value, ignoreCase = true) ||
                    persona.systemMessage.contains(searchQuery.value, ignoreCase = true)

        }

        logger.logVerbose(TAG, "filterPersonas ${matchedPersonas.size}")
    }

    fun onBack() {
        viewModel.persona.restoreState()
        viewModel.navControllerPersona.popBackStack()
    }

    fun deleteAllPersonas() {
        logger.logVerbose(TAG, "deleteAllPersonas")
        viewModel.persona.deleteAllPersonas()
        viewModel.persona.clearSelection()
        showDeleteAllPersonaDialog.value = false
    }

    fun deletePersona(uuid: String) {
        logger.logVerbose(TAG, "deletePersona $uuid")
        viewModelScope.launch {
            viewModel.persona.deletePersona(uuid)
        }
    }

    fun fetchBots() {
        logger.logVerbose(TAG, "fetchBots")
        for (persona in personas.value!!) {
            viewModelScope.launch {
                bots[persona.parentUuid] = botService.getBot(persona.parentUuid)
                logger.logVerbose(TAG, "fetchBots ${persona.parentUuid} ${bots[persona.parentUuid]}")
            }
        }
    }

    fun getBot(uuid: String): BotE? {
        logger.logVerbose(TAG, "getBot $uuid")
        return bots[uuid]
    }

    companion object {
        const val TAG = "PersonaListViewModel"
    }
}
