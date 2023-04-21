// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.repositories.ActivePersonaRepository
import com.mohandass.botforge.chat.repositories.PersonaRepository
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/*
 * A ViewModel to handle active, and available personas
 */
@HiltViewModel
class PersonaViewModel @Inject constructor(
    private val appState: AppState,
    private val personaRepository: PersonaRepository,
    private val activePersonaRepository: ActivePersonaRepository,
    private val logger: Logger,
) : ViewModel() {
    val personaName = activePersonaRepository.activePersonaName
    val personaAlias = activePersonaRepository.activePersonaAlias
    val personaSystemMessage = activePersonaRepository.activePersonaSystemMessage
    val personaUuid = activePersonaRepository.activePersonaUuid
    val personaParentUuid = activePersonaRepository.activePersonaParentUuid

    private val _openDeletePersonaDialog = mutableStateOf(false)
    val openDeleteDialog: State<Boolean> = _openDeletePersonaDialog

    val parentBot = activePersonaRepository.activePersonaParent

    val personas = personaRepository.personas.asLiveData()
    private var _personas = mutableStateListOf<Persona>()

    // Reference:
    // https://stackoverflow.com/questions/48396092/should-i-include-lifecycleowner-in-viewmodel
    private val personasObserver: (List<Persona>) -> Unit = {
        _personas.clear()
        _personas.addAll(it)
    }

    init {
        personas.observeForever(personasObserver)
        _personas = personas.value?.toMutableStateList() ?: mutableStateListOf()
    }

    override fun onCleared() {
        personas.removeObserver(personasObserver)
        super.onCleared()
    }

    fun updateDeletePersonaDialogState(state: Boolean) {
        _openDeletePersonaDialog.value = state
    }

    private val _expandCustomizePersona = mutableStateOf(false)
    val expandCustomizePersona: State<Boolean> = _expandCustomizePersona

    fun updateExpandCustomizePersona(state: Boolean) {
        _expandCustomizePersona.value = state
    }

    private val _chatType = mutableStateOf(ChatType.CREATE)
    val chatType: MutableState<ChatType>
        get() = _chatType

    fun setChatType(chatType: ChatType) {
        _chatType.value = chatType
        FirebaseCrashlytics.getInstance().setCustomKey("chatType", chatType.toString())
    }

    // Move to the Image screen
    fun showImage() {
        logger.logVerbose(TAG, "showImage()")
        // saveState()
        clearSelection()
        setChatType(ChatType.IMAGE)
        if (appState.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Image.route) {
            appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Image.route)
        }
    }

    // Move to List Screen
    fun showList() {
        logger.logVerbose(TAG, "showList()")
        // saveState()
        clearSelection()
        setChatType(ChatType.LIST)
        if (appState.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.List.route) {
            appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.List.route)
        }
    }

    // Move to the History screen
    fun showHistory() {
        logger.logVerbose(TAG, "showHistory()")
        // saveState()
        clearSelection()
        setChatType(ChatType.HISTORY)
        if (appState.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.History.route) {
            appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.History.route)
        }
    }

    // Move to the Create Persona screen
    fun showCreate() {
        logger.logVerbose(TAG, "showCreate()")
        clearSelection()
        setChatType(ChatType.CREATE)
        updateExpandCustomizePersona(true)

        if (appState.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
            appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
        }
    }

    // Move to the Browse Persona screen
    fun showMarketplace() {
        logger.logVerbose(TAG, "showBrowse()")
        clearSelection()
        setChatType(ChatType.BROWSE)

        if (appState.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Marketplace.route) {
            appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Marketplace.route)
        }
    }

    // Move to the Share Persona screen
    fun showSharePersona() {
        logger.logVerbose(TAG, "showSavePersona()")
        // saveState()
        setChatType(ChatType.SHARE)

        appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Share.route)
    }

    // Clears the currently selected persona
    val clearSelection: () -> Unit = {
        activePersonaRepository.clear()
    }

    // Marks a persona as selected
    fun selectPersona(uuid: String) {
        logger.log(TAG, "selectPersona() persona: $uuid")

        val persona = _personas.find { it.uuid == uuid }

        if (persona != null) {
            activePersonaRepository.updateActivePersonaName(persona.name)
            activePersonaRepository.updateActivePersonaUuid(persona.uuid)
            activePersonaRepository.updateActivePersonaAlias(persona.alias)
            activePersonaRepository.updateActivePersonaSystemMessage(persona.systemMessage)
            activePersonaRepository.updateActivePersonaParentUuid(persona.parentUuid)

            chatType.value = ChatType.CHAT
            updateExpandCustomizePersona(false)

            if (appState.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
            }
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
        }
    }

    // Saves a persona to the database, returns true if successful
    private fun savePersona(persona: Persona): Boolean {
        var customMessage = true

        if (personaName.value.isEmpty()) {
            SnackbarManager.showMessage(R.string.persona_name_empty)
            return false
        }
        if (personaSystemMessage.value.trim().isEmpty()) {
            customMessage = false
        }

        viewModelScope.launch {
            personaRepository.addPersona(persona)
            logger.log(TAG, "savePersona() _personas: $persona")
            if (customMessage) {
                SnackbarManager.showMessage(R.string.saved_persona)
            } else {
                SnackbarManager.showMessage(R.string.using_default_system_message)
            }
        }
        return true
    }

    // Saves a copy of the selected persona to the database
    // If the name ends with a number, increment it
    // Otherwise, append " v2" to the name
    fun saveAsNewPersona() {
        var newName = personaName.value

        // If name ends with a number, increment it
        if (newName.last().isDigit()) {
            val number = newName.last().toString().toInt()
            val name = newName.substring(0, newName.length - 1)
            newName = "$name${number + 1}"
        } else {
            newName = "$newName v2"
        }

        // UUID is regenerated, but parent UUID is the same
        val persona = Persona(
            parentUuid = personaParentUuid.value,
            name = newName,
            alias = personaAlias.value,
            systemMessage = personaSystemMessage.value,
        )
        savePersona(persona)
    }

    // Saves a new persona or Updates an existing to the database
    fun saveUpdatePersona() {
        val persona = Persona(
            uuid = personaUuid.value,
            parentUuid = personaParentUuid.value,
            name = personaName.value,
            alias = personaAlias.value,
            systemMessage = personaSystemMessage.value,
        )

        logger.log(TAG, "savePersona() persona: $persona")

        // If alias is empty, generate a random emoji as the alias
        if (persona.alias == "") {
            persona.alias = Utils.randomEmojiUnicode()
            logger.log(TAG, "savePersona() generated alias: ${persona.alias}")
        }

        // If persona is new, generate a UUID and save it
        if (persona.uuid.isEmpty()) {
            logger.log(TAG, "savePersona() new persona")
            val uuid = UUID.randomUUID().toString()
            val isSuccess = savePersona(
                Persona(
                    uuid = uuid,
                    name = persona.name,
                    alias = persona.alias,
                    systemMessage = persona.systemMessage,
                )
            )
            if (isSuccess) {
                activePersonaRepository.updateActivePersonaUuid(uuid)
                chatType.value = ChatType.CHAT
            }
            return
        }

        viewModelScope.launch {
            personaRepository.updatePersona(persona)
            SnackbarManager.showMessage(R.string.saved_persona)
        }
    }

    // Deletes a persona from the database
    fun deletePersona() {
        val persona = _personas.find { it.uuid == personaUuid.value }
        if (persona != null) {
            viewModelScope.launch {
                personaRepository.deletePersona(persona)
                logger.logVerbose(TAG, "deletePersona() _personas: ${persona.name}")
                SnackbarManager.showMessage(R.string.delete_persona_success, persona.name)
            }
            showCreate()
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
        }
    }

    // Deletes all personas saved in the database
    fun deleteAllPersonas() {
        viewModelScope.launch {
            personaRepository.deleteAllPersonas()
            SnackbarManager.showMessage(R.string.delete_all_personas_success)
        }
    }

    fun updatePersonaName(name: String) {
        activePersonaRepository.updateActivePersonaName(name)
    }

    fun updatePersonaAlias(alias: String) {
        activePersonaRepository.updateActivePersonaAlias(alias)
    }

    fun updatePersonaSystemMessage(message: String) {
        activePersonaRepository.updateActivePersonaSystemMessage(message)
    }

    companion object {
        private const val TAG = "PersonaViewModel"
    }
}