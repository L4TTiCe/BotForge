package com.mohandass.botforge.chat.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.model.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.common.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class PersonaViewModel @Inject constructor(
    private val viewModel: AppViewModel,
    private val personaService: PersonaServiceImpl,
    private val logger: Logger,
) : ViewModel() {

    private val _personas = mutableStateListOf<Persona>()
    val personas = _personas

    private val _personaName = mutableStateOf("")
    val personaName: MutableState<String> = _personaName

    private val _personaAlias = mutableStateOf("")
    val personaAlias: MutableState<String> = _personaAlias

    private val _personaSystemMessage = mutableStateOf("")
    val personaSystemMessage: MutableState<String> = _personaSystemMessage

    private val _personaSelected = mutableStateOf("")
    val selectedPersona: MutableState<String> = _personaSelected

    private val _state = mutableStateOf(State())

    class State {
        var chatType: ChatType = ChatType.CREATE
        var personaName: String = ""
        var personaSystemMessage: String = ""
        var personaAlias: String = ""
        var personaSelected: String = ""
    }

    private val _chatType = mutableStateOf(ChatType.CREATE)
    val chatType: MutableState<ChatType>
        get() = _chatType

    private fun setChatType(chatType: ChatType) {
        _chatType.value = chatType
        FirebaseCrashlytics.getInstance().setCustomKey("chatType", chatType.toString())
    }

    fun showHistory() {
        logger.logVerbose(TAG, "showHistory()")
        saveState()
        clearSelection()
        setChatType(ChatType.HISTORY)
        if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.History.route) {
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.History.route)
        }
    }

    fun showCreate() {
        logger.logVerbose(TAG, "showCreate()")
        clearSelection()
        setChatType(ChatType.CREATE)

        if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
        }
    }

    fun showBrowse() {
        logger.logVerbose(TAG, "showBrowse()")
        clearSelection()
        setChatType(ChatType.BROWSE)
    }

    fun saveState() {
        _state.value.chatType = chatType.value
        _state.value.personaName = personaName.value
        _state.value.personaSystemMessage = personaSystemMessage.value
        _state.value.personaAlias = personaAlias.value
        _state.value.personaSelected = selectedPersona.value
    }

    fun restoreState() {
        chatType.value = _state.value.chatType
        personaName.value = _state.value.personaName
        personaSystemMessage.value = _state.value.personaSystemMessage
        personaAlias.value = _state.value.personaAlias
        selectedPersona.value = _state.value.personaSelected
    }

    val clearSelection: () -> Unit = {
        _personaName.value = ""
        _personaAlias.value = ""
        _personaSystemMessage.value = ""
        _personaSelected.value = ""
    }

    fun selectPersona(uuid: String) {
        logger.log(TAG, "selectPersona() persona: $uuid")

//        val persona = _personas.value?.find { it.uuid == uuid }
        val persona = _personas.find { it.uuid == uuid }

        if (persona != null) {
            personaName.value = persona.name
            personaAlias.value = persona.alias
            personaSystemMessage.value = persona.systemMessage
            _personaSelected.value = persona.uuid

            chatType.value = ChatType.CHAT

            if (viewModel.navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
            }
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
        }
    }

    fun fetchPersonas() {
        logger.log(TAG, "fetchPersonas()")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                _personas.postValue(personaService.allPersonas())

                try {
                    _personas.clear()
                } catch (_: Throwable) {}
                _personas.addAll(personaService.allPersonas())
            }
        }
    }

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
            withContext(Dispatchers.IO) {
                personaService.addPersona(persona)
                logger.log(TAG, "savePersona() _personas: $persona")
                if (customMessage) {
                    SnackbarManager.showMessage(R.string.saved_persona)
                } else {
                    SnackbarManager.showMessage(R.string.using_default_system_message)
                }
                fetchPersonas()
            }
        }
        return true
    }

    fun saveAsNewPersona() {
        var newName = _personaName.value

        // If name ends with a number, increment it
        if (newName.last().isDigit()) {
            val number = newName.last().toString().toInt()
            val name = newName.substring(0, newName.length - 1)
            newName= "$name${number + 1}"
        } else {
            newName = "$newName v2"
        }

        val persona = Persona(
            name = newName,
            alias = _personaAlias.value,
            systemMessage = _personaSystemMessage.value,
        )
        savePersona(persona)
    }

    fun saveUpdatePersona() {
        val persona = Persona(
            uuid = _personaSelected.value,
            name = _personaName.value,
            alias = _personaAlias.value,
            systemMessage = _personaSystemMessage.value,
        )

        logger.log(TAG, "savePersona() persona: $persona")

        if (persona.alias == "") {
            persona.alias = Utils.randomEmojiUnicode()
            logger.log(TAG, "savePersona() generated alias: ${persona.alias}")
        }

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
            withContext(Dispatchers.IO) {
                personaService.updatePersona(persona)
                SnackbarManager.showMessage(R.string.saved_persona)
                fetchPersonas()
            }
        }
    }

    fun deletePersona() {
//        val persona = _personas.value?.find { it.uuid == _personaSelected.value }
        val persona = _personas.find { it.uuid == _personaSelected.value }
        if (persona != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    personaService.deletePersona(persona)
                    logger.logVerbose(TAG, "deletePersona() _personas: ${persona.name}")
                    SnackbarManager.showMessage(R.string.delete_persona_success, persona.name)
                    fetchPersonas()
                }
            }
            clearSelection()
        } else {
            SnackbarManager.showMessage(R.string.generic_error)
        }
    }

    fun deleteAllPersonas() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personaService.deleteAllPersonas()
                SnackbarManager.showMessage(R.string.delete_all_personas_success)
                fetchPersonas()
            }
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