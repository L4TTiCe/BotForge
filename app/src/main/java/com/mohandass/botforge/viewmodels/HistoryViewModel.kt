package com.mohandass.botforge.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.logger.Logger
import com.mohandass.botforge.model.Chat
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.service.implementation.ChatServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val chatService: ChatServiceImpl,
    private val appViewModel: AppViewModel,
    private val logger: Logger,
): ViewModel() {
    private val _chats = mutableStateListOf<Chat>()
    val chats = _chats

    fun fetchChats(onSuccess: () -> Unit = {}) {
        logger.log(TAG, "fetchChats()")
        _chats.clear()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _chats.addAll(chatService.getChats())
                logger.logVerbose(TAG, "fetchChats() chats: ${_chats.size}}")
            }
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    private fun isPersonaDeleted(personaUuid: String): Boolean {
        val personas = appViewModel.personas
        personas.firstOrNull { it.uuid == personaUuid } ?: return true
        return false
    }

    private fun fetchMessages(chatUUID: String, onSuccess: () -> Unit) {
        logger.log(TAG, "fetchMessages()")
        viewModelScope.launch {
            val chat = _chats.find { it.uuid == chatUUID }
            var messages: List<Message>
            withContext(Dispatchers.IO) {
                messages = chatService.getMessagesFromChat(chatUUID)

                logger.logVerbose(TAG, "fetchMessages() chat: $chat")
                logger.logVerbose(TAG, "fetchMessages() messages: $messages")

                appViewModel.setMessages(messages)
            }
            withContext(Dispatchers.Main) {
                if (chat?.personaUuid != null) {
                    if (isPersonaDeleted(chat.personaUuid)) {
                        appViewModel.clearSelection(create = false)
                        appViewModel.updatePersonaSystemMessage(messages.first().text)
                        appViewModel.setMessages(messages.subList(1, messages.size))
                    } else {
                        // ignore first message
                        appViewModel.setMessages(messages.subList(1, messages.size))
                        appViewModel.selectPersona(chat.personaUuid)
                    }
                } else {
                    appViewModel.clearSelection(create = false)
                }
                onSuccess()
            }
        }
    }

    fun selectChat(chat: Chat) {
        logger.log(TAG, "selectChat()")

        fetchMessages(chat.uuid) {
            appViewModel.navigateTo(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
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
                logger.log(TAG, "deleteAllChats()")
                chatService.deleteAllChats()
                _chats.clear()
            }
            withContext(Dispatchers.Main) {
                SnackbarManager.showMessage(R.string.delete_all_bookmarked_success)
            }
        }
    }

    fun deleteChat(chatUUID: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logger.log(TAG, "deleteChat() chatUUID: $chatUUID")
                chatService.deleteChatByUUID(chatUUID)
                _chats.removeIf { it.uuid == chatUUID }
            }
        }
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}