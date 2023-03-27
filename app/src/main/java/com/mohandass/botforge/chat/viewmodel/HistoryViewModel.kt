package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.services.implementation.ChatServiceImpl
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.service.Logger
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val chatService: ChatServiceImpl,
    private val viewModel: AppViewModel,
    private val logger: Logger,
) : ViewModel() {
    private val _chats = mutableStateListOf<Chat>()
    val chats = _chats

    private val _openDeleteHistoryDialog = mutableStateOf(false)
    val openDeleteHistoryDialog: State<Boolean> = _openDeleteHistoryDialog

    fun updateDeleteDialogState(state: Boolean) {
        _openDeleteHistoryDialog.value = state
    }

    fun fetchChats(onSuccess: () -> Unit = {}) {
        logger.log(TAG, "fetchChats()")
        _chats.clear()
        viewModelScope.launch {
            _chats.addAll(chatService.getChats())
            logger.logVerbose(TAG, "fetchChats() chats: ${_chats.size}}")

            onSuccess()

        }
    }

    private fun isPersonaDeleted(personaUuid: String): Boolean {
        val personas = viewModel.persona.personas
        personas.firstOrNull { it.uuid == personaUuid } ?: return true
        return false
    }

    private fun fetchMessages(chatUUID: String, onSuccess: () -> Unit) {
        logger.log(TAG, "fetchMessages()")
        viewModelScope.launch {
            val chat = _chats.find { it.uuid == chatUUID }

            val messages: List<Message> = chatService.getMessagesFromChat(chatUUID)

            logger.logVerbose(TAG, "fetchMessages() chat: $chat")
            logger.logVerbose(TAG, "fetchMessages() messages: $messages")

            viewModel.chat.setMessages(messages)


            if (chat?.personaUuid != null) {
                if (isPersonaDeleted(chat.personaUuid)) {
                    viewModel.persona.clearSelection()
                    viewModel.persona.updatePersonaSystemMessage(messages.first().text)
                    viewModel.chat.setMessages(messages.subList(1, messages.size))
                } else {
                    // ignore first message
                    viewModel.chat.setMessages(messages.subList(1, messages.size))
                    viewModel.persona.selectPersona(chat.personaUuid)
                }
            } else {
                viewModel.persona.clearSelection()
            }
            onSuccess()

        }
    }

    fun selectChat(chat: Chat) {
        logger.log(TAG, "selectChat()")

        fetchMessages(chat.uuid) {
            viewModel.navigateTo(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
        }

    }

    fun getMessagesCount(chatUUID: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val count = chatService.getMessagesCount(chatUUID)
            onSuccess(count)
        }
    }

    fun deleteAllChats() {
        viewModelScope.launch {
            logger.log(TAG, "deleteAllChats()")
            chatService.deleteAllChats()
            _chats.clear()
            SnackbarManager.showMessage(R.string.delete_all_bookmarked_success)

        }
    }

    fun deleteChat(chatUUID: String) {
        viewModelScope.launch {

            logger.log(TAG, "deleteChat() chatUUID: $chatUUID")
            chatService.deleteChatByUUID(chatUUID)
            _chats.removeIf { it.uuid == chatUUID }

        }
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}