// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.ExportedChat
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.services.implementation.ChatServiceImpl
import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.common.services.FileUtils
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
 * A ViewModel to handle the History Screen
 */
class HistoryViewModel(
    private val chatService: ChatServiceImpl,
    private val viewModel: AppViewModel,
    private val logger: Logger,
    private val analytics: Analytics,
) : ViewModel() {
    private val _chats = mutableStateListOf<Chat>()
    val chats = _chats

    private val _openDeleteHistoryDialog = mutableStateOf(false)
    val openDeleteHistoryDialog: State<Boolean> = _openDeleteHistoryDialog

    fun updateDeleteDialogState(state: Boolean) {
        _openDeleteHistoryDialog.value = state
    }

    // Fetch all saved chats from the database
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

    // Get the messages for the selected chat
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

    // Select a chat and navigate to the chat screen
    // Loads the messages for the selected chat, sets the persona and navigates to the chat screen
    fun selectChat(chat: Chat) {
        logger.log(TAG, "selectChat()")

        fetchMessages(chat.uuid) {
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route)
        }

    }

    fun exportChat(chatUUID: String, context: Context) {
        viewModelScope.launch {
            val chat = _chats.find { it.uuid == chatUUID }
            val messages: List<Message> = chatService.getMessagesFromChat(chatUUID)

            val data = ExportedChat(
                chatInfo = chat,
                messageCount = messages.size,
                messages = messages
            )

            FileUtils.exportChatAsPdf(
                title = chat?.name ?: "ChatExport",
                chatInfo = data,
                context = context
            )

            analytics.logPdfExported()
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

    private lateinit var deleteJob: Job

    // Starts a Coroutine to delete the chat, that will delete the chat after 5 seconds
    // If the user cancels the delete operation, the chat will not be deleted
    //
    // In the meanwhile,
    // the chat will be removed from the list (only UI) and the user will be notified
    fun deleteChat(chatUUID: String) {
        deleteJob = viewModelScope.launch {
            logger.log(TAG, "deleteChat() chatUUID: $chatUUID")
            _chats.removeIf { it.uuid == chatUUID }

            SnackbarManager.showMessageWithAction(
                R.string.delete_bookmarked_success,
                R.string.undo,
                this@HistoryViewModel::cancelDeleteChat
            )

            delay(5000)

            chatService.deleteChatByUUID(chatUUID)
        }
    }

    // Cancel the delete chat operation
    private fun cancelDeleteChat() {
        deleteJob.cancel()
        fetchChats()
        SnackbarManager.showMessage(R.string.delete_bookmarked_cancelled)
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}