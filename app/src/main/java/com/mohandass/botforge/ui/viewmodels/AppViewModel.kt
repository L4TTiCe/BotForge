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

    private val _personas = MutableLiveData<List<Persona>>()


    // getters
    val personas: MutableLiveData<List<Persona>>
        get() = _personas

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

    fun savePersona() {
        var customMessage = true

        if (_personaName.value.isEmpty()) {
            SnackbarManager.showMessage(AppText.persona_name_empty)
            return
        }
        if (_personaSystemMessage.value.trim().isEmpty()) {
            customMessage = false
        }

        val persona = Persona(
            name = _personaName.value,
            systemMessage = _personaSystemMessage.value
        )

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

    private val _personaName = mutableStateOf("")
    val personaName: MutableState<String> = _personaName

    private val _personaSystemMessage = mutableStateOf("")
    val personaSystemMessage: MutableState<String> = _personaSystemMessage

    private val _activeChat = mutableStateOf(listOf(Message()))
    val activeChat: MutableState<List<Message>> = _activeChat
    fun updatePersonaName(name: String) {
        _personaName.value = name
    }
    fun updatePersonaSystemMessage(message: String) {
        _personaSystemMessage.value = message
    }

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
