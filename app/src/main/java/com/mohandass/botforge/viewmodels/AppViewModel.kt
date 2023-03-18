package com.mohandass.botforge.viewmodels

import androidx.compose.runtime.MutableState
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
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val accountService: AccountService,
    personaService: PersonaServiceImpl,
    openAiService: OpenAiService,
    chatService: ChatServiceImpl,
    preferencesDataStore: PreferencesDataStore,
    private val logger: Logger,
) : ViewModel() {
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

    // History

    private val _historyViewModel: HistoryViewModel = HistoryViewModel(
        viewModel = this,
        chatService = chatService,
        logger = logger
    )
    val history: HistoryViewModel
        get() = _historyViewModel

    // State

    private val _isLoading = mutableStateOf(false)
    val isLoading: MutableState<Boolean>
        get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

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
        var chatType: ChatType = ChatType.CREATE
    }

    private val _state = mutableStateOf(State())

    fun saveState() {
        logger.log(TAG, "saveState()")
        persona.saveState()
        _state.value.chatType = _chatType.value
    }

    fun restoreState() {
        logger.log(TAG, "restoreState()")
        persona.restoreState()
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

    private val _personaViewModel = PersonaViewModel(
        viewModel = this,
        personaService = personaService,
        logger = logger
    )
    val persona: PersonaViewModel
        get() = _personaViewModel

    fun clearSelection(create:Boolean = true) {
        logger.log(TAG, "newPersona()")
        persona.clearSelection()

        if (create) {
            chatType.value = ChatType.CREATE
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

    companion object {
        private const val TAG = "AppViewModel"
    }
}
