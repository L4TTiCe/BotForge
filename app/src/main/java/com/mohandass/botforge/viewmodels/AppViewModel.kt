package com.mohandass.botforge.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.SnackbarMessage.Companion.toSnackbarMessageWithAction
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.model.entities.Persona
import com.mohandass.botforge.model.service.AccountService
import com.mohandass.botforge.model.service.implementation.OpenAiService
import com.mohandass.botforge.model.service.implementation.PersonaServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import com.mohandass.botforge.R.string as AppText

@HiltViewModel
class AppViewModel @Inject constructor(
    private val accountService: AccountService,
    private val personaService: PersonaServiceImpl,
    private val openAiService: OpenAiService
)
: ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: MutableState<Boolean>
        get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    // OpenAI

    fun getChatCompletion() {
        Log.v("AppViewModel", "getChatCompletion()")
        setLoading(true)

        val messages = mutableListOf<Message>()
        if (_personaSystemMessage.value != "") {
            messages.add(Message(_personaSystemMessage.value, Role.SYSTEM))
        } else {
            messages.add(Message("You are a helpful assistant.", Role.SYSTEM))
        }

        for (message in _activeChat.value) {
            if (message.isActive) {
                messages.add(message)
                Log.v("AppViewModel", "getChatCompletion() message: $message")
            }
        }

        Log.v("AppViewModel", "getChatCompletion() messages: $messages")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val completion = openAiService.getChatCompletion(messages)
                    Log.v("AppViewModel", "getChatCompletion() completion: $completion")
                    addMessage(completion)
                } catch (e: Throwable) {
                    Log.e("AppViewModel", "getChatCompletion() error: $e")
                    e.printStackTrace()
                    if (e.message != null) {
                        SnackbarManager.showMessage(
                            e.toSnackbarMessageWithAction(R.string.settings) {
                            navigateTo(AppRoutes.MainRoutes.Settings.route)
                        })
                    } else {
                        val message = Utils.parseStackTraceForErrorMessage(e)
                        SnackbarManager.showMessage(
                            message.toSnackbarMessageWithAction(R.string.settings) {
                                navigateTo(AppRoutes.MainRoutes.Settings.route)
                            })
                    }
                }
                setLoading(false)
            }
        }
    }


    // Navigation
    // NacController in MainUI.kt
    private var _navController: NavController? = null
    val navController: NavController
        get() = _navController!!

    fun setNavController(navController: NavController) {
        Log.v("AppViewModel", "setNavController()")
        _navController = navController
    }

    fun navigateTo(route: String) {
        Log.v("AppViewModel", "navigateTo($route)")
        navController.navigate(route)
    }

    // Persona

    private val _personas = MutableLiveData<List<Persona>>()
    val personas: MutableLiveData<List<Persona>>
        get() = _personas

    private val _personaName = mutableStateOf("")
    val personaName: MutableState<String> = _personaName

    private val _personaAlias = mutableStateOf("")
    val personaAlias: MutableState<String> = _personaAlias

    private val _personaSystemMessage = mutableStateOf("")
    val personaSystemMessage: MutableState<String> = _personaSystemMessage

    private val _personaSelected = mutableStateOf("")
    val selectedPersona: MutableState<String> = _personaSelected

    init {
        Log.v("AppViewModel", "init()")
        fetchPersonas()
    }

    private fun fetchPersonas() {
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
        _personaAlias.value = ""
        _personaSystemMessage.value = ""
        _personaSelected.value = ""
    }

    fun selectPersona(uuid: String) {
        Log.v("AppViewModel", "selectPersona() persona: $uuid")

        val persona = _personas.value?.find { it.uuid == uuid }
        if (persona != null) {
            personaName.value = persona.name
            personaAlias.value = persona.alias
            personaSystemMessage.value = persona.systemMessage
            _personaSelected.value = persona.uuid
        } else {
            SnackbarManager.showMessage(AppText.generic_error)
        }
    }

    private fun savePersona(persona: Persona): Boolean {
        var customMessage = true

        if (_personaName.value.isEmpty()) {
            SnackbarManager.showMessage(AppText.persona_name_empty)
            return false
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

        if (persona.uuid.isEmpty()) {
            Log.v("AppViewModel", "savePersona() new persona")
            val uuid = UUID.randomUUID().toString()
            val isSuccess = savePersona(
                Persona(
                    uuid = uuid,
                    name = _personaName.value,
                    alias = _personaAlias.value,
                    systemMessage = _personaSystemMessage.value,
                )
            )
            if (isSuccess) {
                _personaSelected.value = uuid
            }
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

    fun deletePersona() {
        val persona = _personas.value?.find { it.uuid == _personaSelected.value }
        if (persona != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    personaService.deletePersona(persona)
                    Log.v("AppViewModel", "deletePersona() _personas: ${persona.name}")
                    SnackbarManager.showMessage(AppText.delete_persona_success, persona.name)
                    fetchPersonas()
                }
            }
            newPersona()
        } else {
            SnackbarManager.showMessage(AppText.generic_error)
        }
    }

    fun deleteAllPersonas() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personaService.deleteAllPersonas()
                SnackbarManager.showMessage(AppText.delete_all_personas_success)
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

    // Message

    private val _activeChat = mutableStateOf(listOf(Message()))
    val activeChat: MutableState<List<Message>> = _activeChat

    private val _handleDelete = mutableStateOf(false)
    val handleDelete: MutableState<Boolean> = _handleDelete

    fun handleDelete(handle: Boolean) {
        _handleDelete.value = handle
    }

    fun autoAddMessage() {
        Log.v("AppViewModel", "autoAddMessage()")
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

    private fun addMessage(message: Message) {
        Log.v("AppViewModel", "addMessage()")
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

    fun clearMessages() {
        Log.v("AppViewModel", "clearMessages()")
        while (_activeChat.value.isNotEmpty()) {
            deleteMessage(_activeChat.value.last())
        }
        autoAddMessage()
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

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                accountService.deleteAccount()
            }
            withContext(Dispatchers.Main) {
                SnackbarManager.showMessage(R.string.delete_account_success)
                onSuccess()
            }
        }
    }

}
