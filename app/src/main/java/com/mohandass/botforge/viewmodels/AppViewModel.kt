package com.mohandass.botforge.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.SnackbarMessage.Companion.toSnackbarMessageWithAction
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.model.Chat
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.model.entities.Persona
import com.mohandass.botforge.model.service.AccountService
import com.mohandass.botforge.model.service.OpenAiService
import com.mohandass.botforge.model.service.implementation.ChatServiceImpl
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
    private val openAiService: OpenAiService,
    private val chatService: ChatServiceImpl
)
: ViewModel() {
    private val _historyViewModel: HistoryViewModel = HistoryViewModel(
        appViewModel = this,
        chatService = chatService)
    val historyViewModel: HistoryViewModel
        get() = _historyViewModel

    private val _isLoading = mutableStateOf(false)
    val isLoading: MutableState<Boolean>
        get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    // OpenAI

    fun getChatCompletion(hapticFeedback: HapticFeedback) {
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
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                setLoading(false)
            }
        }
    }

    // State

    enum class ChatType {
        CREATE,
        CHAT,
        HISTORY
    }

    private val _chatType = mutableStateOf(ChatType.CREATE)
    val chatType: MutableState<ChatType>
        get() = _chatType

    fun setChatType(chatType: ChatType) {
        _chatType.value = chatType
    }


    // Navigation
    // NacController in MainUI.kt
    private var _navControllerMain: NavController? = null
    val navControllerMain: NavController
        get() = _navControllerMain!!

    private var _navControllerPersona: NavController? = null
    val navControllerPersona: NavController
        get() = _navControllerPersona!!

    fun setNavControllerMain(navController: NavController) {
        Log.v("AppViewModel", "setNavControllerMain()")
        _navControllerMain = navController
    }

    fun setNavControllerPersona(navController: NavController) {
        Log.v("AppViewModel", "setNavControllerPersona()")
        _navControllerPersona = navController
    }

    fun navigateTo(route: String) {
        Log.v("AppViewModel", "navigateTo($route)")
        navControllerMain.navigate(route)
    }

    fun showHistory() {
        Log.v("AppViewModel", "showHistory()")
        clearSelection(create = false)
        setChatType(ChatType.HISTORY)
        if (navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.History.route) {
            navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.History.route)
        }
    }

    fun showCreate() {
        Log.v("AppViewModel", "showCreate()")
        clearSelection(create = true)
        setChatType(ChatType.CREATE)

        if (navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
            navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
        }
    }

    // Persona

//    private val _personas = MutableLiveData<List<Persona>>()
//    val personas: MutableLiveData<List<Persona>>
//        get() = _personas
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

    init {
        Log.v("AppViewModel", "init()")
        fetchPersonas()
    }

    private fun fetchPersonas() {
        Log.v("AppViewModel", "fetchPersonas()")
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

    fun clearSelection(create:Boolean = true) {
        Log.v("AppViewModel", "newPersona()")
        _personaName.value = ""
        _personaAlias.value = ""
        _personaSystemMessage.value = ""
        _personaSelected.value = ""

        if (create) {
            chatType.value = ChatType.CREATE
        }
    }

    fun selectPersona(uuid: String) {
        Log.v("AppViewModel", "selectPersona() persona: $uuid")

//        val persona = _personas.value?.find { it.uuid == uuid }
        val persona = _personas.find { it.uuid == uuid }

        if (persona != null) {
            personaName.value = persona.name
            personaAlias.value = persona.alias
            personaSystemMessage.value = persona.systemMessage
            _personaSelected.value = persona.uuid

            chatType.value = ChatType.CHAT

            if (navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
            }
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
//        val persona = _personas.value?.find { it.uuid == _personaSelected.value }
        val persona = _personas.find { it.uuid == _personaSelected.value }
        if (persona != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    personaService.deletePersona(persona)
                    Log.v("AppViewModel", "deletePersona() _personas: ${persona.name}")
                    SnackbarManager.showMessage(AppText.delete_persona_success, persona.name)
                    fetchPersonas()
                }
            }
            clearSelection()
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
//        val message = if (_activeChat.value.isEmpty()) {
//            Message("", Role.USER)
//        } else {
//            Message(
//                "",
//                if (_activeChat.value.last().role == Role.USER) Role.BOT else Role.USER
//            )
//        }
        val message = Message("", Role.USER)
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

    fun deleteMessage(messageUuid: String) {
        Log.v("AppViewModel", "Deleting message: $messageUuid")
        _activeChat.value = _activeChat.value.filter {
            it.uuid != messageUuid
        }
        Log.v("AppViewModel", "Messages: ${activeChat.value}")
    }

    fun clearMessages() {
        Log.v("AppViewModel", "clearMessages()")
        while (_activeChat.value.isNotEmpty()) {
            deleteMessage(_activeChat.value.last().uuid)
        }

        autoAddMessage()
    }

    private val _chatName = mutableStateOf("testChat")
    val chatName: MutableState<String> = _chatName

    fun updateChatName(name: String) {
        _chatName.value = name
    }

    fun saveChat() {
        val chat = Chat(
            uuid = UUID.randomUUID().toString(),
            name = _chatName.value,
            personaUuid = _personaSelected.value,
        )

        for (message in _activeChat.value) {
            Log.v("AppViewModel", "saveChat() message: $message")
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatService.saveChat(chat, _activeChat.value)
                SnackbarManager.showMessage(AppText.chat_saved)
            }
        }
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
