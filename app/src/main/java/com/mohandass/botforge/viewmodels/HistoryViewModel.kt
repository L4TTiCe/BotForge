package com.mohandass.botforge.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.model.Chat
import com.mohandass.botforge.model.service.implementation.ChatServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val chatService: ChatServiceImpl,
    private val appViewModel: AppViewModel
): ViewModel() {
    private val _chats = mutableStateListOf<Chat>()
    val chats = _chats

    fun fetchChats(onSuccess: () -> Unit = {}) {
        Log.v(TAG, "fetchChats()")
        _chats.clear()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _chats.addAll(chatService.getChats())
                Log.v(TAG, "fetchChats() chats: ${_chats.size}}")
            }
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun getMessagesCount(chatUUID: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val count = chatService.getMessagesCount(chatUUID)
                onSuccess(count)
            }
        }
    }

    fun deleteAllChats() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatService.deleteAllChats()
                _chats.clear()
            }
        }
    }

    fun deleteChat(chatUUID: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatService.deleteChatByUUID(chatUUID)
                _chats.removeIf { it.uuid == chatUUID }
            }
        }
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}