// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.perf.metrics.AddTrace
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.ExportedChat
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.chat.repositories.ActiveMessagesRepository
import com.mohandass.botforge.chat.repositories.ActivePersonaRepository
import com.mohandass.botforge.common.services.OpenAiService
import com.mohandass.botforge.chat.services.implementation.ChatServiceImpl
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.common.services.FileUtils
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.services.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.mohandass.botforge.common.services.snackbar.SnackbarMessage.Companion.toSnackbarMessageWithAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/*
 * A ViewModel to handle the chat Screen
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val appState: AppState,
    private val activePersonaRepository: ActivePersonaRepository,
    private val activeMessagesRepository: ActiveMessagesRepository,
    private val accountService: AccountService,
    private val openAiService: OpenAiService,
    private val chatService: ChatServiceImpl,
    private val logger: Logger,
    private val analytics: Analytics,
) : ViewModel() {

    // Dialog State
    private val _openAliasDialog = mutableStateOf(false)
    val openAliasDialog: State<Boolean> = _openAliasDialog

    fun updateAliasDialogState(state: Boolean) {
        _openAliasDialog.value = state
    }

    private val _openSaveChatDialog = mutableStateOf(false)
    val openSaveChatDialog: State<Boolean> = _openSaveChatDialog

    fun updateSaveChatDialogState(state: Boolean) {
        _openSaveChatDialog.value = state
    }

    private val _isMessageInFocus = mutableStateOf(false)
    val isMessageInFocus: MutableState<Boolean> = _isMessageInFocus

    private lateinit var job: Job

    // Interrupts the current request, if any, to the OpenAI API
    private fun interruptRequest() {
        if (this::job.isInitialized) {
            job.cancel()
        }
        setLoading(false)
    }

    // Confirms if the user wants to cancel the current request
    fun handleInterrupt() {
        SnackbarManager.showMessageWithAction(R.string.waiting_for_response, R.string.cancel) {
            interruptRequest()
        }
    }

    // Shows the time elapsed since starting the request
    private lateinit var timerJob: Job

    private val _isLoading = appState.isChatLoading
    val isLoading: State<Boolean>
        get() = _isLoading

    private fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading

        if (isLoading) {
            updateLastTimestamp()
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(100)
                    _timeMillis.value = Date().time - _lastTimestamp.value
                }
            }
        } else {
            timerJob.cancel()
            _timeMillis.value = 0
        }
    }

    private val _lastTimestamp = mutableStateOf(0L)
    private val _timeMillis = mutableStateOf(0L)
    val timeMillis: State<Long> = _timeMillis

    private fun updateLastTimestamp() {
        _lastTimestamp.value = Date().time
    }

    // Uses the ChatService to request Chat Completion from the OpenAI API
    @AddTrace(name = "getChatCompletion", enabled = true)
    fun getChatCompletion(
        onComplete: () -> Unit
    ) {
        logger.log(TAG, "getChatCompletion()")
        setLoading(true)

        // Construct the request
        val messages = activeMessagesRepository.getAllMessages(
            includeSystemMessage = true,
            includeInactiveMessages = false
        )

        job = viewModelScope.launch {
            try {
                val completion = openAiService.getChatCompletion(messages)
                logger.logVerbose(TAG, "getChatCompletion() completion: $completion")
                addMessage(completion)
            } catch (e: Throwable) {
                logger.logError(TAG, "getChatCompletion() error: $e", e)
                if (e.message != null) {
                    logger.logError(TAG, "getChatCompletion() error m: ${e.message}", e)
                    SnackbarManager.showMessage(
                        e.toSnackbarMessageWithAction(R.string.settings) {
                            appState.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
                        })
                } else {
                    logger.logError(TAG, "getChatCompletion() error st: ${e.stackTrace}", e)
                    val message = Utils.parseStackTraceForErrorMessage(e)

                    // Attempt to parse the error message
                    when (message.message) {
                        Utils.INVALID_API_KEY_ERROR_MESSAGE -> {
                            SnackbarManager.showMessageWithAction(
                                R.string.invalid_api_key,
                                R.string.settings
                            ) {
                                appState.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
                            }
                        }

                        Utils.INTERRUPTED_ERROR_MESSAGE -> {
                            SnackbarManager.showMessage(R.string.request_cancelled)
                        }

                        else -> {
                            SnackbarManager.showMessage(
                                message.toSnackbarMessageWithAction(R.string.settings) {
                                    appState.navControllerMain.navigate(AppRoutes.MainRoutes.Settings.route)
                                })
                        }
                    }
                }
            }
            onComplete()
            setLoading(false)
        }
    }

    // Message
    val activeChat = activeMessagesRepository.activeChat
    private var _activeChatBackup = mutableStateOf(listOf(Message()))

    private val _handleDelete = mutableStateOf(false)
    val handleDelete: MutableState<Boolean> = _handleDelete

    fun handleDelete(handle: Boolean) {
        _handleDelete.value = handle
    }

    fun autoAddMessage() {
        logger.log(TAG, "autoAddMessage()")
        val message = Message("", Role.USER)
        activeMessagesRepository.addMessage(message)
    }

    private fun addMessage(message: Message) {
        logger.log(TAG, "addMessage()")
        activeMessagesRepository.addMessage(message)
    }

    fun updateMessage(message: Message) {
        activeMessagesRepository.updateMessage(message)
    }

    fun deleteMessage(messageUuid: String) {
        activeMessagesRepository.deleteMessage(messageUuid)
    }

    // Clears all messages, and adds a new empty message
    // Also shows a snackbar with an undo action
    fun clearMessages() {
        val isChatEmpty = activeChat.value.isEmpty() || activeChat.value[0].text.isBlank()

        // Make a copy (backup) of the current chat before clearing it
        // if not empty
        if (isChatEmpty.not()) {
            _activeChatBackup.value = activeChat.value
        }

        logger.log(TAG, "clearMessages()")
        while (activeChat.value.isNotEmpty()) {
            deleteMessage(activeChat.value.last().uuid)
        }

        autoAddMessage()

        if (isChatEmpty.not()) {
            SnackbarManager.showMessageWithAction(
                R.string.active_chat_cleared,
                R.string.undo
            ) {
                undoClearMessages()
            }
        }
    }

    // Restores the chat to the state before it was cleared
    private fun undoClearMessages() {
        logger.log(TAG, "undoClearMessages()")
        activeMessagesRepository.setMessages(_activeChatBackup.value)
    }

    private val _chatName = mutableStateOf("testChat")

    fun updateChatName(name: String) {
        _chatName.value = name
    }

    fun exportAsPdf(context: Context) {
        val messages = activeMessagesRepository.getAllMessages(
            includeSystemMessage = true,
            includeInactiveMessages = true
        )

        val data = ExportedChat(
            messageCount = messages.size,
            messages = messages
        )

        FileUtils.exportChatAsPdf(
            chatInfo = data,
            context = context
        )

        analytics.logPdfExported()
    }

    fun exportChatAsJson(context: Context) {
        val messages = activeMessagesRepository.getAllMessages(
            includeSystemMessage = true,
            includeInactiveMessages = true
        )

        val data = ExportedChat(
            messageCount = messages.size,
            messages = messages
        )

        FileUtils.exportChatAsJson(
            jsonString = data.toPrettyJson(),
            context = context
        )

        analytics.logJsonExported()
    }

    // Saves the current chat to the database
    @AddTrace(name = "saveChat", enabled = true)
    fun saveChat() {
        val personaSelected = activePersonaRepository.activePersonaUuid.value
        val messages = activeMessagesRepository.getAllMessages(
            includeSystemMessage = true,
            includeInactiveMessages = true
        )

        val chat = Chat(
            uuid = UUID.randomUUID().toString(),
            name = _chatName.value,
            personaUuid =
            if (personaSelected == "") {
                null
            } else {
                personaSelected
            }
        )

        viewModelScope.launch {
            chatService.saveChat(chat, messages)
            SnackbarManager.showMessageWithAction(
                R.string.chat_saved,
                R.string.bookmarks,
            ) {
                appState.navControllerPersona
                    .navigate(AppRoutes.MainRoutes.PersonaRoutes.History.route)
            }
        }
    }

    // Generates a chat name using API
    fun generateChatName(
        onComplete: (String) -> Unit
    ) {
        val systemMessageInitial = Message(
            text = appState.resources.getString(R.string.system_message_chat_name_initial),
            role = Role.SYSTEM
        )

        val messages = mutableListOf(systemMessageInitial)
        messages.addAll(
            activeMessagesRepository.getAllMessages(
                includeSystemMessage = false,
                includeInactiveMessages = false
            )
        )

        val systemMessageFinal = Message(
            text = appState.resources.getString(R.string.system_message_chat_name_final),
            role = Role.SYSTEM
        )
        messages.add(systemMessageFinal)

        viewModelScope.launch {
            try {
                val chatName = openAiService.getChatCompletion(messages)
                onComplete(chatName.text)
            } catch (e: Exception) {
                logger.logError(TAG, "Error generating chat name: ${e.message}")
                onComplete("")
                SnackbarManager.showMessage(
                    e.toSnackbarMessage()
                )
            }
        }
    }

    fun signOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            accountService.signOut()
            onSuccess()
        }
    }

    companion object {
        private const val TAG = "ChatViewModel"
    }
}
