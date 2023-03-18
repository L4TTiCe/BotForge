package com.mohandass.botforge.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.logger.Logger
import com.mohandass.botforge.model.entities.Persona
import com.mohandass.botforge.model.preferences.UserPreferences
import com.mohandass.botforge.model.service.AccountService
import com.mohandass.botforge.model.service.OpenAiService
import com.mohandass.botforge.model.service.PreferencesDataStore
import com.mohandass.botforge.model.service.implementation.ChatServiceImpl
import com.mohandass.botforge.model.service.implementation.PersonaServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import com.mohandass.botforge.R.string as AppText

@HiltViewModel
class AppViewModel @Inject constructor(
    private val accountService: AccountService,
    private val personaService: PersonaServiceImpl,
    openAiService: OpenAiService,
    chatService: ChatServiceImpl,
    preferencesDataStore: PreferencesDataStore,
    private val logger: Logger,
)
: ViewModel() {
//    val initialSetupEvent = liveData {
//        emit(preferencesDataStore.fetchInitialPreferences())
//    }

    // Keep the user preferences as a stream of changes
    private val userPreferencesFlow = preferencesDataStore.userPreferencesFlow

    private val _userPreferencesFlow = userPreferencesFlow.map {userPreference ->
        UserPreferences(
            preferredTheme = userPreference.preferredTheme,
            useDynamicColors = userPreference.useDynamicColors
        )
    }

    val userPreferences = _userPreferencesFlow.asLiveData()

    private val _historyViewModel: HistoryViewModel = HistoryViewModel(
        viewModel = this,
        chatService = chatService,
        logger = logger
    )
    val history: HistoryViewModel
        get() = _historyViewModel

    private val _isLoading = mutableStateOf(false)
    val isLoading: MutableState<Boolean>
        get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
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

    private fun setChatType(chatType: ChatType) {
        _chatType.value = chatType
        FirebaseCrashlytics.getInstance().setCustomKey("chatType", chatType.toString())
    }

    class State {
        var _personaName: String = ""
        var _personaSystemMessage: String = ""
        var _personaAlias: String = ""
        var _personaSelected: String = ""
        var chatType: ChatType = ChatType.CREATE
    }

    private val _state = mutableStateOf(State())

    fun saveState() {
        logger.log(TAG, "saveState()")
        _state.value._personaName = _personaName.value
        _state.value._personaSystemMessage = _personaSystemMessage.value
        _state.value._personaAlias = _personaAlias.value
        _state.value._personaSelected = _personaSelected.value
        _state.value.chatType = _chatType.value
    }

    fun restoreState() {
        logger.log(TAG, "restoreState()")
        _personaName.value = _state.value._personaName
        _personaSystemMessage.value = _state.value._personaSystemMessage
        _personaAlias.value = _state.value._personaAlias
        _personaSelected.value = _state.value._personaSelected
        _chatType.value = _state.value.chatType
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
        logger.logVerbose(TAG, "setNavControllerMain()")
        _navControllerMain = navController
    }

    fun setNavControllerPersona(navController: NavController) {
        logger.logVerbose(TAG, "setNavControllerPersona()")
        _navControllerPersona = navController
    }

    fun navigateTo(route: String) {
        logger.log(TAG, "navigateTo($route)")
        navControllerMain.navigate(route)
    }

    fun showHistory() {
        logger.logVerbose(TAG, "showHistory()")
        saveState()
        clearSelection(create = false)
        setChatType(ChatType.HISTORY)
        if (navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.History.route) {
            navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.History.route)
        }
    }

    fun showCreate() {
        logger.logVerbose(TAG, "showCreate()")
        clearSelection(create = true)
        setChatType(ChatType.CREATE)

        if (navControllerPersona.currentDestination?.route != AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
            navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
        }
    }

    // Chat

    private val _chatViewModel = ChatViewModel(
        viewModel = this,
        chatService = chatService,
        openAiService = openAiService,
        logger = logger
    )
    val chat: ChatViewModel
        get() = _chatViewModel

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

//    init {
//        Log.v("AppViewModel", "init()")
//        fetchPersonas()
//    }

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

    fun clearSelection(create:Boolean = true) {
        logger.log(TAG, "newPersona()")
        _personaName.value = ""
        _personaAlias.value = ""
        _personaSystemMessage.value = ""
        _personaSelected.value = ""

        if (create) {
            chatType.value = ChatType.CREATE
        }
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
                logger.log(TAG, "savePersona() _personas: $persona")
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
            logger.log(TAG, "savePersona() new persona")
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
                chatType.value = ChatType.CHAT
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
                    logger.logVerbose(TAG, "deletePersona() _personas: ${persona.name}")
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

    companion object {
        private const val TAG = "AppViewModel"
    }
}
