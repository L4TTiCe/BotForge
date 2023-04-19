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
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.repositories.PersonaRepository
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.service.BotService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/*
 * A ViewModel to handle active, and available personas
 */
class PersonaViewModel @Inject constructor(
    private val viewModel: AppViewModel,
    private val personaService: PersonaRepository,
    private val botService: BotService,
    private val logger: Logger,
) : ViewModel() {
    private val _personaName = mutableStateOf("")
    val personaName: MutableState<String> = _personaName

    private val _personaAlias = mutableStateOf("")
    val personaAlias: MutableState<String> = _personaAlias

    private val _personaSystemMessage = mutableStateOf("")
    val personaSystemMessage: MutableState<String> = _personaSystemMessage

    private val _personaSelected = mutableStateOf("")
    val selectedPersona: MutableState<String> = _personaSelected

    private val _personaSelectedParentUuid = mutableStateOf("")
    val selectedPersonaParentUuid: State<String> = _personaSelectedParentUuid

    private val _openDeletePersonaDialog = mutableStateOf(false)
    val openDeleteDialog: State<Boolean> = _openDeletePersonaDialog

    private val _parentBot: MutableState<BotE?> = mutableStateOf(null)
    val parentBot: State<BotE?> = _parentBot

    val personas = personaService.personas.asLiveData()
    private var _personas = mutableStateListOf<Persona>()

    // Reference:
    // https://stackoverflow.com/questions/48396092/should-i-include-lifecycleowner-in-viewmodel
    private val observer: (List<Persona>) -> Unit = {
        _personas.clear()
        _personas.addAll(it)
    }

    init {
        personas.observeForever(observer)
        _personas = personas.value?.toMutableStateList() ?: mutableStateListOf()
    }

    override fun onCleared() {
        personas.removeObserver(observer)
        super.onCleared()
    }


    private fun updateParentBot(bot: BotE?) {
        _parentBot.value = bot
    }

    private fun updateSelectedPersonaParentUuid(uuid: String) {
        _personaSelectedParentUuid.value = uuid

        if (uuid.isNotEmpty() || uuid.isNotBlank()) {
            viewModelScope.launch {
                updateParentBot(botService.getBot(uuid))
                logger.logVerbose("PersonaViewModel", "updateSelectedPersonaParentUuid: ${parentBot.value}")
            }
        } else {
            updateParentBot(null)
            logger.logVerbose("PersonaViewModel", "updateSelectedPersonaParentUuid: ${parentBot.value}")
        }
    }

    fun updateDeletePersonaDialogState(state: Boolean) {
        _openDeletePersonaDialog.value = state
    }

    private val _expandCustomizePersona = mutableStateOf(false)
    val expandCustomizePersona: State<Boolean> = _expandCustomizePersona

    fun updateExpandCustomizePersona(state: Boolean) {
        _expandCustomizePersona.value = state
    }

    private val _state = mutableStateOf(UiState())

    // Represents the current state of the UI
    class UiState {
        var personaName: String = ""
        var personaSystemMessage: String = ""
        var personaAlias: String = ""
        var personaSelected: String = ""
        var personaSelectedParentUuid: String = ""
    }

    private val _chatType = mutableStateOf(ChatType.CREATE)
    val chatType: MutableState<ChatType>
        get() = _chatType

    fun setChatType(chatType: ChatType) {
        _chatType.value = chatType
        FirebaseCrashlytics.getInstance().setCustomKey("chatType", chatType.toString())
    }

    // Move to List Screen
    fun showList() {
        logger.logVerbose(TAG, "showList()")
        saveState()
        clearSelection()
        setChatType(ChatType.LIST)
        if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.List.route) {
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.List.route)
        }
    }

    // Move to the History screen
    fun showHistory() {
        logger.logVerbose(TAG, "showHistory()")
        saveState()
        clearSelection()
        setChatType(ChatType.HISTORY)
        if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.History.route) {
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.History.route)
        }
    }

    // Move to the Create Persona screen
    fun showCreate() {
        logger.logVerbose(TAG, "showCreate()")
        clearSelection()
        setChatType(ChatType.CREATE)
        updateExpandCustomizePersona(true)

        if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
        }
    }

    // Move to the Browse Persona screen
    fun showMarketplace() {
        logger.logVerbose(TAG, "showBrowse()")
        clearSelection()
        setChatType(ChatType.BROWSE)

        if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Marketplace.route) {
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Marketplace.route)
        }
    }

    // Move to the Share Persona screen
    fun showSharePersona() {
        logger.logVerbose(TAG, "showSavePersona()")
        saveState()
        setChatType(ChatType.SHARE)

        viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Share.route)
    }

    // Saves the current state of the UI
    fun saveState() {
        _state.value.personaName = personaName.value
        _state.value.personaSystemMessage = personaSystemMessage.value
        _state.value.personaAlias = personaAlias.value
        _state.value.personaSelected = selectedPersona.value
        _state.value.personaSelectedParentUuid = selectedPersonaParentUuid.value
    }

    // Restores the state of the UI from the last saved state
    fun restoreState() {
        personaName.value = _state.value.personaName
        personaSystemMessage.value = _state.value.personaSystemMessage
        personaAlias.value = _state.value.personaAlias
        selectedPersona.value = _state.value.personaSelected
        updateSelectedPersonaParentUuid(_state.value.personaSelectedParentUuid)
    }

    // Clears the currently selected persona
    val clearSelection: () -> Unit = {
        _personaName.value = ""
        _personaAlias.value = ""
        _personaSystemMessage.value = ""
        _personaSelected.value = ""
        updateSelectedPersonaParentUuid("")
    }

    // Marks a persona as selected
    fun selectPersona(uuid: String) {
        logger.log(TAG, "selectPersona() persona: $uuid")

        val persona = _personas.find { it.uuid == uuid }

        if (persona != null) {
            personaName.value = persona.name
            personaAlias.value = persona.alias
            personaSystemMessage.value = persona.systemMessage
            _personaSelected.value = persona.uuid
            updateSelectedPersonaParentUuid(persona.parentUuid)

            chatType.value = ChatType.CHAT
            updateExpandCustomizePersona(false)

            if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
            }
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
        }
    }

    // Saves a persona to the database, returns true if successful
    private fun savePersona(persona: Persona): Boolean {
        var customMessage = true

        if (_personaName.value.isEmpty()) {
            SnackbarManager.showMessage(R.string.persona_name_empty)
            return false
        }
        if (_personaSystemMessage.value.trim().isEmpty()) {
            customMessage = false
        }

        viewModelScope.launch {
            personaService.addPersona(persona)
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
        var newName = _personaName.value

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
            parentUuid = selectedPersonaParentUuid.value,
            name = newName,
            alias = _personaAlias.value,
            systemMessage = _personaSystemMessage.value,
        )
        savePersona(persona)
    }

    // Saves a new persona or Updates an existing to the database
    fun saveUpdatePersona() {
        val persona = Persona(
            uuid = _personaSelected.value,
            parentUuid = selectedPersonaParentUuid.value,
            name = _personaName.value,
            alias = _personaAlias.value,
            systemMessage = _personaSystemMessage.value,
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
                _personaSelected.value = uuid
                chatType.value = ChatType.CHAT
            }
            return
        }

        viewModelScope.launch {
            personaService.updatePersona(persona)
            SnackbarManager.showMessage(R.string.saved_persona)
        }
    }

    // Deletes a persona from the database
    fun deletePersona() {
        val persona = _personas.find { it.uuid == _personaSelected.value }
        if (persona != null) {
            viewModelScope.launch {
                personaService.deletePersona(persona)
                logger.logVerbose(TAG, "deletePersona() _personas: ${persona.name}")
                SnackbarManager.showMessage(R.string.delete_persona_success, persona.name)
            }
            showCreate()
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
        }
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
                personaService.deletePersona(persona)
                logger.logVerbose(TAG, "deletePersona() _personas: ${persona.name}")
            }
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
        }
    }

    // Deletes all personas saved in the database
    fun deleteAllPersonas() {
        viewModelScope.launch {
            personaService.deleteAllPersonas()
            SnackbarManager.showMessage(R.string.delete_all_personas_success)
        }
    }

    fun updatePersonaName(name: String) {
        _personaName.value = name
    }

    fun updatePersonaAlias(alias: String) {
        _personaAlias.value = alias
    }

    fun updatePersonaSystemMessage(message: String) {
        _personaSystemMessage.value = message
    }

    companion object {
        private const val TAG = "PersonaViewModel"
    }
}