package com.mohandass.botforge

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.chat.model.services.OpenAiService
import com.mohandass.botforge.chat.model.services.implementation.ChatServiceImpl
import com.mohandass.botforge.chat.model.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.chat.viewmodel.ChatViewModel
import com.mohandass.botforge.chat.viewmodel.HistoryViewModel
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.chat.viewmodel.TopBarViewModel
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.settings.model.UserPreferences
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
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
