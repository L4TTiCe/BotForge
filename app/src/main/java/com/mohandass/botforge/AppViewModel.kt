package com.mohandass.botforge

import android.content.res.Resources
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.settings.model.UserPreferences
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.chat.model.services.OpenAiService
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
import com.mohandass.botforge.chat.model.services.implementation.ChatServiceImpl
import com.mohandass.botforge.chat.model.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.chat.ui.viewmodel.ChatViewModel
import com.mohandass.botforge.chat.ui.viewmodel.HistoryViewModel
import com.mohandass.botforge.chat.ui.viewmodel.PersonaViewModel
import com.mohandass.botforge.chat.ui.viewmodel.TopBarViewModel
import com.mohandass.botforge.common.SnackbarMessage.Companion.getDismissAction
import com.mohandass.botforge.common.SnackbarMessage.Companion.getDismissLabel
import com.mohandass.botforge.common.SnackbarMessage.Companion.hasAction
import com.mohandass.botforge.common.SnackbarMessage.Companion.toMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
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

    // Snackbar
    private val snackbarManager: SnackbarManager = SnackbarManager

    private lateinit var snackbarHostState: SnackbarHostState
    private lateinit var resources: Resources

    fun initSnackbar(snackbarHostState: SnackbarHostState, resources: Resources) {
        this.resources = resources
        this.snackbarHostState = snackbarHostState

        viewModelScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                val hasDismissAction = snackbarMessage.hasAction()
                if (hasDismissAction) {
                    val dismissLabel = snackbarMessage.getDismissLabel(resources)
                    val dismissAction = snackbarMessage.getDismissAction()

                    val result: SnackbarResult = snackbarHostState.showSnackbar(
                        message = text,
                        actionLabel = dismissLabel,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        dismissAction()
                    }
                } else {
                    snackbarHostState.showSnackbar(
                        text,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }



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

    // TopBar

    private val _topBarViewModel: TopBarViewModel = TopBarViewModel()
    val topBar: TopBarViewModel
        get() = _topBarViewModel


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
        BROWSE,
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
    private var _navController: NavHostController? = null
    val navController: NavHostController
        get() = _navController!!

    // NacController in MainUI.kt
    private var _navControllerMain: NavController? = null
    val navControllerMain: NavController
        get() = _navControllerMain!!

    private var _navControllerPersona: NavController? = null
    val navControllerPersona: NavController
        get() = _navControllerPersona!!

    fun setNavController(navController: NavHostController) {
        logger.logVerbose(TAG, "setNavController()")
        _navController = navController
    }
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

    fun showBrowse() {
        logger.logVerbose(TAG, "showBrowse()")
        clearSelection(create = false)
        setChatType(ChatType.BROWSE)
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
