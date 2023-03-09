package com.mohandass.botforge.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.model.entities.Persona
import com.mohandass.botforge.model.service.AccountService
import com.mohandass.botforge.model.service.implementation.PersonaServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.mohandass.botforge.R.string as AppText

@HiltViewModel
class AppViewModel @Inject constructor(
    private val accountService: AccountService,
    private val personaService: PersonaServiceImpl
)
: ViewModel() {

    // Persona

    private val _personas = MutableLiveData<List<Persona>>()
    val personas: MutableLiveData<List<Persona>>
        get() = _personas

    private val _personaName = mutableStateOf("")
    val personaName: MutableState<String> = _personaName

    private val _personaSystemMessage = mutableStateOf("")
    val personaSystemMessage: MutableState<String> = _personaSystemMessage

    private val _personaSelected = mutableStateOf("")
    val personaSelected: MutableState<String> = _personaSelected

    init {
        Log.v("AppViewModel", "init()")
        fetchPersonas()
    }

    fun fetchPersonas() {
        Log.v("AppViewModel", "fetchPersonas()")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _personas.postValue(personaService.allPersonas())
            }
        }
    }

    fun newPersona() {
        Log.v("AppViewModel", "newPersona()")
        _personaName.value = ""
        _personaSystemMessage.value = ""
        _personaSelected.value = ""
    }

    fun selectPersona(uuid: String) {
        Log.v("AppViewModel", "selectPersona() persona: $uuid")

        val persona = _personas.value?.find { it.uuid == uuid }
        if (persona != null) {
            personaName.value = persona.name
            personaSystemMessage.value = persona.systemMessage
            _personaSelected.value = persona.uuid
        } else {
            SnackbarManager.showMessage(AppText.generic_error)
        }
    }

    private fun savePersona(persona: Persona) {
        var customMessage = true

        if (_personaName.value.isEmpty()) {
            SnackbarManager.showMessage(AppText.persona_name_empty)
            return
        }
        if (_personaSystemMessage.value.trim().isEmpty()) {
            customMessage = false
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personaService.addPersona(persona)
                Log.v("AppViewModel", "savePersona() _personas: $persona")
                if (customMessage) {
                    SnackbarManager.showMessage(AppText.saved_persona)
                } else {
                    SnackbarManager.showMessage(AppText.using_default_system_message)
                }
                fetchPersonas()
            }
        }
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
            systemMessage = _personaSystemMessage.value,
        )
        savePersona(persona)
    }

    fun saveNewPersona() {
        val persona = Persona(
            uuid = _personaSelected.value,
            name = _personaName.value,
            systemMessage = _personaSystemMessage.value,
        )

        if (persona.uuid.isEmpty()) {
            Log.v("AppViewModel", "savePersona() new persona")
            savePersona(
                Persona(
                    name = _personaName.value,
                    systemMessage = _personaSystemMessage.value,
                )
            )
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personaService.updatePersona(persona)
                SnackbarManager.showMessage(AppText.saved_persona)
                fetchPersonas()
            }
        }
    }

    fun updatePersonaName(name: String) {
        _personaName.value = name
    }
    fun updatePersonaSystemMessage(message: String) {
        _personaSystemMessage.value = message
    }

    // Message

    private val _activeChat = mutableStateOf(listOf(Message()))
    val activeChat: MutableState<List<Message>> = _activeChat

    fun autoAddMessage() {
        val message = if (_activeChat.value.isEmpty()) {
            Message("", Role.USER)
        } else {
            Message(
                "",
                if (_activeChat.value.last().role == Role.USER) Role.BOT else Role.USER
            )
        }
        _activeChat.value = _activeChat.value + message
    }

    fun updateMessage(message: Message) {
        _activeChat.value = _activeChat.value.map {
            if (it.uuid == message.uuid) {
                message
            } else {
                it
            }
        }
    }

    fun deleteMessage(message: Message) {
        Log.v("AppViewModel", "Messages: ${activeChat.value}")
        Log.v("AppViewModel", "Deleting message: ${message.uuid}")
        _activeChat.value = _activeChat.value.filter {
            it.uuid != message.uuid
        }
        Log.v("AppViewModel", "Messages: ${activeChat.value}")
    }

    // Account

    fun signOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                accountService.signOut()
            }
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

}