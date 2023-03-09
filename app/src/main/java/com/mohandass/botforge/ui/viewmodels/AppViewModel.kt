package com.mohandass.botforge.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val accountService: AccountService
)
: ViewModel() {

    private val _personaName = mutableStateOf("")
    val personaName: MutableState<String> = _personaName

    private val _personaSystemMessage = mutableStateOf("")
    val personaSystemMessage: MutableState<String> = _personaSystemMessage

    private val _activeChat = mutableStateOf(listOf(Message()))
    val activeChat: MutableState<List<Message>> = _activeChat
    fun updatePersonaName(name: String) {
        _personaName.value = name
    }
    fun updatePersonaSystemMessage(message: String) {
        _personaSystemMessage.value = message
    }

    fun addMessage(message: Message) {
        _activeChat.value = _activeChat.value + message
    }

    fun autoAddMessage() {
        val message = if (_activeChat.value.isEmpty()) {
            Message("", Role.USER)
        } else {
            Message(
                "",
                if (_activeChat.value.last().role == Role.USER) Role.BOT else Role.USER
            )
        }
        _activeChat.value = _activeChat.value + message
    }

    fun updateMessage(message: Message) {
        _activeChat.value = _activeChat.value.map {
            if (it.uuid == message.uuid) {
                message
            } else {
                it
            }
        }
    }

    fun deleteMessage(message: Message) {
        Log.v("AppViewModel", "Messages: ${activeChat.value}")
        Log.v("AppViewModel", "Deleting message: ${message.uuid}")
        _activeChat.value = _activeChat.value.filter {
            it.uuid != message.uuid
        }
        Log.v("AppViewModel", "Messages: ${activeChat.value}")
    }

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

}
