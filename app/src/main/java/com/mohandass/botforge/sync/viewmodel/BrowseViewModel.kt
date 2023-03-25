package com.mohandass.botforge.sync.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.service.BotServiceImpl
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class BrowseViewModel @Inject constructor (
    private val viewModel: AppViewModel,
    private val botService: BotServiceImpl,
    private val logger: Logger,
): ViewModel() {
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private val _fetchedBots = mutableListOf<List<BotE>>(listOf())

    fun search() {
        viewModelScope.launch {
            _fetchedBots.clear()
//            _fetchedBots.addAll(listOf(botService.searchBots( searchQuery.value )))
            _fetchedBots.addAll(listOf(botService.searchBots( Utils.sanitizeSearchQuery(searchQuery.value) )))
            Log.v(TAG, "search: $_fetchedBots")
        }
    }

    fun addBot() {
        var bot = BotE(
            uuid = UUID.randomUUID().toString(),
            name = "Personal Assistant",
            alias = "ChatGPT",
            systemMessage = "You are Chat GPT",
            description = "generic chatbot",
            tags = listOf("tag1", "TAG2"),
            createdBy = "admin",
            usersCount = 0,
            userUpVotes = 0,
            userDownVotes = 0,
            createdAt = Date().time,
            updatedAt = Date().time,
        )

        viewModelScope.launch {
            botService.addBot(bot)
        }

        bot = BotE(
            uuid = UUID.randomUUID().toString(),
            name = "HealthGPT",
            alias = "myHealthBot",
            systemMessage = "You are HealthGPT-2.",
            description = "Your personal bot to deduce your health and fitness",
            tags = listOf("Healthcare", "Education"),
            createdBy = "admin",
            usersCount = 0,
            userUpVotes = 0,
            userDownVotes = 0,
            createdAt = Date().time,
            updatedAt = Date().time,
        )

        viewModelScope.launch {
            botService.addBot(bot)
        }
    }

    fun getBots() {
        viewModelScope.launch {
            _fetchedBots.clear()
            _fetchedBots.addAll(listOf(botService.getAllBots()))
            Log.v(TAG, "getBots: $_fetchedBots")
        }
    }

    fun deleteAllBots() {
        viewModelScope.launch {
            botService.deleteAllBots()
        }
    }

    companion object {
        private const val TAG = "BrowseViewModel"
    }
}