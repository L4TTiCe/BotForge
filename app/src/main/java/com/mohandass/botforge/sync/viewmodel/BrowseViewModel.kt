// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.perf.metrics.AddTrace
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.chat.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.PreferencesDataStore
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.service.BotService
import com.mohandass.botforge.sync.service.FirestoreService
import com.mohandass.botforge.sync.service.implementation.FirebaseDatabaseServiceImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Browse screen.
 *
 * Handles fetching and searching of bots from the Community, stored locally in the Room database.
 * Also handles the syncing of bots from the Community to the local database.
 */
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

    private val _recentBots = mutableStateListOf<BotE>()
    val recentBots = _recentBots

    private val _randomBots = mutableStateListOf<BotE>()
    val randomBots = _randomBots

    // Populate the top bots list with the bots from the Community
    fun fetchBots() {
        logger.logVerbose(TAG, "fetchBots()")
        viewModelScope.launch {
            _topBots.clear()
            _topBots.addAll(botService.getBots())
        }
        viewModelScope.launch {
            _recentBots.clear()
            _recentBots.addAll(botService.getMostRecentBots())
        }
        viewModelScope.launch {
            _randomBots.clear()
            _randomBots.addAll(botService.getRandomBots())
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

    // Clears the fetched bots list and fetches new bots from the Community,
    // based on the search query.
    private fun search() {
        viewModelScope.launch {
            _fetchedBots.clear()
            _fetchedBots.addAll(botService.searchBots(Utils.sanitizeSearchQuery(searchQuery.value)))
            logger.logVerbose(TAG, "search: $_fetchedBots")
        }
    }

    // Makes a persona from the bot and adds it to the local database
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

    // Syncs bots from the Community to the local database
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

            applyContentModeration()
        }
    }

    // Deletes bots from the local database that have been marked for deletion in the Community
    private fun applyContentModeration() {
        val lastModerationIndexProcessed =
            viewModel.userPreferences.value?.lastModerationIndexProcessed
        logger.log(TAG, "applyContentModeration: " +
                "lastModerationIndexProcessed: $lastModerationIndexProcessed")
        viewModelScope.launch {
            val deletionRecords = firebaseDatabaseService.fetchBotsDeletedAfter(
                lastModerationIndexProcessed!!
            )

            if (deletionRecords.isNotEmpty()) {
                for (deletionRecord in deletionRecords) {
                    deletionRecord.botId?.let { botService.deleteBot(it) }
                }

                logger.log(TAG, "applyContentModeration: " +
                        "deletionRecordsCount: ${deletionRecords.size}")

                // Find the largest index in the deletion records
                val maxIndex = deletionRecords.maxByOrNull { it.index!! }?.index

                // Update the last moderation index processed
                preferencesDataStore.setLastModerationIndexProcessed(maxIndex!!)

                fetchBots()
            }
        }
    }

    companion object {
        private const val TAG = "BrowseViewModel"
    }
}
