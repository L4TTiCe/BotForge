package com.mohandass.botforge.sync.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.perf.metrics.AddTrace
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.chat.model.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.service.BotService
import com.mohandass.botforge.sync.model.service.FirestoreService
import com.mohandass.botforge.sync.model.service.implementation.FirebaseDatabaseServiceImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

class BrowseViewModel @Inject constructor(
    private val viewModel: AppViewModel,
    private val botService: BotService,
    private val personaService: PersonaServiceImpl,
    private val firebaseDatabaseService: FirebaseDatabaseServiceImpl,
    private val firestoreService: FirestoreService,
    private val accountService: AccountService,
    private val preferencesDataStore: PreferencesDataStore,
    private val logger: Logger,
) : ViewModel() {
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _topBots = mutableStateListOf<BotE>()
    val topBots = _topBots

    fun fetchBots() {
        logger.logVerbose(TAG, "fetchBots()")
        viewModelScope.launch {
            _topBots.clear()
            _topBots.addAll(botService.getBots())
        }
    }

    init {
        fetchBots()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModel.browse.search()
    }

    private val _fetchedBots = mutableStateListOf<BotE>()
    val fetchedBots = _fetchedBots

    fun search() {
        viewModelScope.launch {
            _fetchedBots.clear()
            _fetchedBots.addAll(botService.searchBots(Utils.sanitizeSearchQuery(searchQuery.value)))
            logger.logVerbose(TAG, "search: $_fetchedBots")
        }
    }

    fun makePersona(bot: BotE) {
        logger.logVerbose(TAG, "makePersona()")
        viewModelScope.launch {
            personaService.addPersona(bot.toPersona())
            viewModel.persona.fetchPersonas()
        }
    }

    fun upVote(botId: String) {
        logger.logVerbose(TAG, "upVote()")
        viewModelScope.launch {
            firestoreService.addUpVote(botId, accountService.currentUserId)
        }
    }

    fun downVote(botId: String) {
        logger.logVerbose(TAG, "downVote()")
        viewModelScope.launch {
            firestoreService.addDownVote(botId, accountService.currentUserId)
        }
    }

    fun report(botId: String) {
        logger.logVerbose(TAG, "report()")
        viewModelScope.launch {
            botService.deleteBot(botId)
            firestoreService.addReport(botId, accountService.currentUserId)
            fetchBots()
        }
    }

    fun deleteAllBots() {
        logger.log(TAG, "deleteAllBots()")
        viewModelScope.launch {
            botService.deleteAllBots()
            SnackbarManager.showMessage(R.string.clear_all_bots_toast)
        }
    }

    @AddTrace(name = "syncWithDatabase", enabled = true)
    fun syncWithDatabase() {
        val lastSyncedAt = viewModel.userPreferences.value?.lastSuccessfulSync
        logger.log(TAG, "syncWithDatabase: lastSyncedAt: $lastSyncedAt")
        viewModelScope.launch {
            val bots = firebaseDatabaseService.fetchBotsUpdatedAfter(lastSyncedAt!!)
            for (bot in bots) {
                botService.addBot(bot.toBotE())
            }
            if (bots.isNotEmpty()) {
                preferencesDataStore.updateLastSuccessfulSync()
            }
            fetchBots()
        }
    }

    companion object {
        private const val TAG = "BrowseViewModel"
    }
}