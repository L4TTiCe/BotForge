package com.mohandass.botforge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.chat.services.OpenAiService
import com.mohandass.botforge.chat.services.implementation.ChatServiceImpl
import com.mohandass.botforge.chat.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.chat.viewmodel.ChatViewModel
import com.mohandass.botforge.chat.viewmodel.HistoryViewModel
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.chat.viewmodel.TopBarViewModel
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.SnackbarMessage.Companion.toSnackbarMessageWithAction
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.model.UserPreferences
import com.mohandass.botforge.settings.service.PreferencesDataStore
import com.mohandass.botforge.sync.service.BotService
import com.mohandass.botforge.sync.service.FirestoreService
import com.mohandass.botforge.sync.service.implementation.FirebaseDatabaseServiceImpl
import com.mohandass.botforge.sync.viewmodel.BrowseViewModel
import com.mohandass.botforge.sync.viewmodel.SharePersonaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the AppState
 *
 * Holds references to the services that are used throughout the app, and
 * other viewModels.
 *
 * Could be considered the "root" ViewModel of the app [!] BAD PRACTICE
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val accountService: AccountService,
    personaService: PersonaServiceImpl,
    openAiService: OpenAiService,
    chatService: ChatServiceImpl,
    botService: BotService,
    preferencesDataStore: PreferencesDataStore,
    firebaseDatabaseServiceImpl: FirebaseDatabaseServiceImpl,
    firestoreService: FirestoreService,
    val logger: Logger,
) : ViewModel() {

    // Keep the user preferences as a stream of changes
    private val userPreferencesFlow = preferencesDataStore.userPreferencesFlow

    private val _userPreferencesFlow = userPreferencesFlow.map { userPreference ->
        UserPreferences(
            preferredTheme = userPreference.preferredTheme,
            useDynamicColors = userPreference.useDynamicColors,
            lastSuccessfulSync = userPreference.lastSuccessfulSync,
            enableUserGeneratedContent = userPreference.enableUserGeneratedContent,
            enableShakeToClear = userPreference.enableShakeToClear,
            shakeToClearSensitivity = userPreference.shakeToClearSensitivity
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
        botService = botService,
        logger = logger
    )
    val persona: PersonaViewModel
        get() = _personaViewModel

    // Sync

    private val _browseViewModel = BrowseViewModel(
        viewModel = this,
        botService = botService,
        personaService = personaService,
        firebaseDatabaseService = firebaseDatabaseServiceImpl,
        firestoreService = firestoreService,
        accountService = accountService,
        preferencesDataStore = preferencesDataStore,
        logger = logger
    )
    val browse: BrowseViewModel
        get() = _browseViewModel

    private val _sharePersonaViewModel = SharePersonaViewModel(
        viewModel = this,
        accountService = accountService,
        firebaseDatabaseServiceImpl = firebaseDatabaseServiceImpl,
        logger = logger
    )
    val sharePersona: SharePersonaViewModel
        get() = _sharePersonaViewModel

    // Account
    fun signOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            accountService.signOut()
            onSuccess()
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                accountService.deleteAccount()
                SnackbarManager.showMessage(R.string.delete_account_success)
                onSuccess()
            } catch (e: Exception) {
                e.toSnackbarMessageWithAction(R.string.sign_out) {
                    signOut(onSuccess)
                }.let { message ->
                    SnackbarManager.showMessage(message)
                }

            }
        }
    }

    companion object {
        private const val TAG = "AppViewModel"
    }
}
