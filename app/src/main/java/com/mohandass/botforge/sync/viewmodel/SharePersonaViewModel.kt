// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.chat.repositories.ActivePersonaRepository
import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.sync.model.Bot
import com.mohandass.botforge.sync.service.implementation.FirebaseDatabaseServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for the Share Persona screen.
 *
 * Handles the sharing of a persona to the Community.
 */
@HiltViewModel
class SharePersonaViewModel @Inject constructor(
    private val appState: AppState,
    private val accountService: AccountService,
    private val activePersonaRepository: ActivePersonaRepository,
    private val firebaseDatabaseServiceImpl: FirebaseDatabaseServiceImpl,
    private val logger: Logger,
    private val analytics: Analytics
) : ViewModel() {

    val backHandler = {
        appState.navControllerPersona.popBackStack()
    }

    private fun clear() {
        _personaDescription.value = ""
        _currentTag.value = ""
        _personaTags = mutableStateListOf()
    }

    private val _personaDescription = mutableStateOf("")
    val personaDescription: State<String> = _personaDescription

    fun updatePersonaDescription(value: String) {
        _personaDescription.value = value
    }

    private val _currentTag = mutableStateOf("")
    val currentTag: State<String> = _currentTag

    fun updateCurrentTag(value: String) {
        val tags = value.split(" ", ",", "\n")

        if (tags.size > 1) {
            for (i in 0 until tags.size - 1) {
                if (tags[i].isNotEmpty())
                    _personaTags.add(tags[i])
            }

            // Update current tag with the last tag
            _currentTag.value = tags[tags.size - 1]
        } else
            _currentTag.value = value
    }

    private var _personaTags = mutableStateListOf<String>()
    val personaTags: SnapshotStateList<String> = _personaTags

    fun removeTag(tag: String) {
        _personaTags.remove(tag)
    }

    // Share Persona
    fun shareBot() {
        val bot = Bot(
            uuid = UUID.randomUUID().toString(),
            parentUuid = activePersonaRepository.activePersonaUuid.value,
            name = activePersonaRepository.activePersonaName.value,
            alias = activePersonaRepository.activePersonaAlias.value,
            systemMessage = activePersonaRepository.activePersonaSystemMessage.value,
            description = _personaDescription.value,
            tags = _personaTags.toList().joinToString(","),
            createdBy = accountService.displayName,
            usersCount = 0,
            userUpVotes = 0,
            userDownVotes = 0,
            createdAt = Date().time,
            updatedAt = Date().time,
        )

        viewModelScope.launch {
            firebaseDatabaseServiceImpl.writeNewBot(bot)
            analytics.logBotSharedWithCommunity()
            SnackbarManager.showMessage(R.string.share_persona_success)
            clear()
            backHandler()
        }
    }

    companion object {
        private const val TAG = "SharePersonaViewModel"
    }
}
