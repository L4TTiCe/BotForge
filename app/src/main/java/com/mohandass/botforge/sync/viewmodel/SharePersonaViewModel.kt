package com.mohandass.botforge.sync.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.sync.model.Bot
import com.mohandass.botforge.sync.model.service.implementation.FirebaseDatabaseServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SharePersonaViewModel @Inject constructor (
    private val viewModel: AppViewModel,
    private val accountService: AccountService,
    private val firebaseDatabaseServiceImpl: FirebaseDatabaseServiceImpl,
    private val logger: Logger,
): ViewModel() {

    val backHandler = {
        viewModel.persona.restoreState()
        viewModel.navControllerPersona.popBackStack()
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

        if(tags.size > 1) {
            for(i in 0 until tags.size - 1) {
                if(tags[i].isNotEmpty())
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

    fun shareBot() {
        val bot = Bot(
            uuid = UUID.randomUUID().toString(),
            name = viewModel.persona.personaName.value,
            alias = viewModel.persona.personaAlias.value,
            systemMessage = viewModel.persona.personaSystemMessage.value,
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
            withContext(Dispatchers.IO) {
                firebaseDatabaseServiceImpl.writeNewBot(bot)
            }
            withContext(Dispatchers.Main) {
                SnackbarManager.showMessage(R.string.share_persona_success)
                clear()
                backHandler()
            }
        }
    }

    companion object {
        private const val TAG = "SharePersonaViewModel"
    }
}