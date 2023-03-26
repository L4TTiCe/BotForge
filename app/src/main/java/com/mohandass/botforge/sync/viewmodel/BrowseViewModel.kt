package com.mohandass.botforge.sync.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.chat.model.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.service.BotServiceImpl
import com.mohandass.botforge.sync.service.FirebaseDatabaseServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BrowseViewModel @Inject constructor (
    private val viewModel: AppViewModel,
    private val botService: BotServiceImpl,
    private val personaService: PersonaServiceImpl,
    private val firebaseDatabaseService: FirebaseDatabaseServiceImpl,
    private val preferencesDataStore: PreferencesDataStore,
    private val logger: Logger,
): ViewModel() {
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
    }

    private val _fetchedBots = mutableStateListOf<BotE>()
    val fetchedBots = _fetchedBots

    fun search() {
        viewModelScope.launch {
            _fetchedBots.clear()
            _fetchedBots.addAll(botService.searchBots( Utils.sanitizeSearchQuery(searchQuery.value)))
            Log.v(TAG, "search: $_fetchedBots")
        }
    }

    fun makePersona(bot: BotE) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personaService.addPersona(bot.toPersona())
                viewModel.persona.fetchPersonas()
            }
        }
    }

//    fun addBot() {
//        var bot = BotE(
//            uuid = UUID.randomUUID().toString(),
//            name = "Personal Assistant",
//            alias = "ChatGPT",
//            systemMessage = "You are Chat GPT",
//            description = "generic chatbot",
//            tags = listOf("tag1", "TAG2"),
//            createdBy = "admin",
//            usersCount = 0,
//            userUpVotes = 0,
//            userDownVotes = 0,
//            createdAt = Date().time,
//            updatedAt = Date().time,
//        )
//
//        viewModelScope.launch {
//            botService.addBot(bot)
//        }
//
//        bot = BotE(
//            uuid = UUID.randomUUID().toString(),
//            name = "HealthGPT",
//            alias = "myHealthBot",
//            systemMessage = "You are HealthGPT-2.",
//            description = "Your personal bot to deduce your health and fitness",
//            tags = listOf("Healthcare", "Education"),
//            createdBy = "admin",
//            usersCount = 0,
//            userUpVotes = 0,
//            userDownVotes = 0,
//            createdAt = Date().time,
//            updatedAt = Date().time,
//        )
//
//        viewModelScope.launch {
//            botService.addBot(bot)
//        }
//    }

//    fun getBots() {
//        viewModelScope.launch {
//            _fetchedBots.clear()
//            _fetchedBots.addAll(botService.getAllBots())
//            Log.v(TAG, "getBots: $_fetchedBots")
//        }
//    }
//
//    fun deleteAllBots() {
//        viewModelScope.launch {
//            botService.deleteAllBots()
//        }
//    }

    fun syncWithDatabase() {
        val lastSyncedAt = viewModel.userPreferences.value?.lastSuccessfulSync
        logger.log(TAG, "syncWithDatabase: lastSyncedAt: $lastSyncedAt")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesDataStore.updateLastSuccessfulSync()
                val bots = firebaseDatabaseService.fetchBotsUpdatedAfter(lastSyncedAt!!)
                for (bot in bots) {
                    botService.addBot(bot.toBotE())
                }
                fetchBots()
            }
        }
    }

    companion object {
        private const val TAG = "BrowseViewModel"
    }
}