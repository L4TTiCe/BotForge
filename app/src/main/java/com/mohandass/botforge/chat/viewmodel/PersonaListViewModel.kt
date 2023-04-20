// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.repositories.PersonaRepository
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.service.BotService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * A ViewModel to handle the Persona List Screen
 */
@HiltViewModel
class PersonaListViewModel @Inject constructor (
    private val appState: AppState,
    private val personaRepository: PersonaRepository,
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
        appState.navControllerPersona.popBackStack()
    }

    fun deleteAllPersonas() {
        logger.logVerbose(TAG, "deleteAllPersonas")
        viewModelScope.launch {
            personaRepository.deleteAllPersonas()
            SnackbarManager.showMessage(R.string.delete_all_personas_success)
        }
        showDeleteAllPersonaDialog.value = false
    }

    fun deletePersona(uuid: String) {
        var deleteJob: Job = Job()

        val persona = _personas.find { it.uuid == uuid }
        if (persona != null) {
            // Remove persona from the list
            _personas.remove(persona)

            SnackbarManager.showMessageWithAction(
                R.string.deleted_persona,
                R.string.undo
            ) {
                _personas.add(persona)
                deleteJob.cancel()
            }

            deleteJob = viewModelScope.launch {
                delay(5000)
                personaRepository.deletePersona(persona)
                logger.logVerbose(TAG, "deletePersona() _personas: ${persona.name}")
            }
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
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
