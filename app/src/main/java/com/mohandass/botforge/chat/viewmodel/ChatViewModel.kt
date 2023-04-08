// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.perf.metrics.AddTrace
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.chat.services.OpenAiService
import com.mohandass.botforge.chat.services.implementation.ChatServiceImpl
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.services.snackbar.SnackbarMessage.Companion.toSnackbarMessageWithAction
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/*
 * A ViewModel to handle the chat Screen
 */
class ChatViewModel @Inject constructor(
    private val viewModel: AppViewModel,
    private val openAiService: OpenAiService,
    private val chatService: ChatServiceImpl,
    private val logger: Logger,
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

    private val _requestInProgress = mutableStateOf(false)

    private val _isMessageInFocus = mutableStateOf(false)
    val isMessageInFocus: MutableState<Boolean> = _isMessageInFocus

    private lateinit var job: Job

    // Interrupts the current request, if any, to the OpenAI API
    private fun interruptRequest() {
        if (_requestInProgress.value) {
            _requestInProgress.value = false
        }
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

    private val _isLoading = mutableStateOf(false)
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
    fun getChatCompletion(onComplete: () -> Unit) {
        logger.log(TAG, "getChatCompletion()")
        setLoading(true)

        // Construct the request
        val messages = mutableListOf<Message>()
        val personaSystemMessage = viewModel.persona.personaSystemMessage.value

        // If no persona is selected, use the default persona
        if (personaSystemMessage != "") {
            messages.add(Message(personaSystemMessage, Role.SYSTEM))
        } else {
            messages.add(Message("You are a helpful assistant.", Role.SYSTEM))
        }

        for (message in _activeChat.value) {
            if (message.isActive) {
                messages.add(message)
                logger.logVerbose(TAG, "getChatCompletion() message: $message")
            }
        }

        logger.logVerbose(TAG, "getChatCompletion() messages: $messages")

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
                            viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
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
                                viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
                            }
                        }
                        Utils.INTERRUPTED_ERROR_MESSAGE -> {
                            SnackbarManager.showMessage(R.string.request_cancelled)
                        }
                        else -> {
                            SnackbarManager.showMessage(
                                message.toSnackbarMessageWithAction(R.string.settings) {
                                    viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.Settings.route)
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

    private val _activeChat = mutableStateOf(listOf(Message()))
    val activeChat: MutableState<List<Message>> = _activeChat

    private var _activeChatBackup = mutableStateOf(listOf(Message()))

    private val _handleDelete = mutableStateOf(false)
    val handleDelete: MutableState<Boolean> = _handleDelete

    fun handleDelete(handle: Boolean) {
        _handleDelete.value = handle
    }

    fun autoAddMessage() {
        logger.log(TAG, "autoAddMessage()")
        val message = Message("", Role.USER)
        _activeChat.value = _activeChat.value + message
    }

    private fun addMessage(message: Message) {
        logger.log(TAG, "addMessage()")
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

    fun deleteMessage(messageUuid: String) {
        logger.log(TAG, "Deleting message: $messageUuid")
        _activeChat.value = _activeChat.value.filter {
            it.uuid != messageUuid
        }
        logger.logVerbose(TAG, "Messages: ${activeChat.value}")
    }

    // Clears all messages, and adds a new empty message
    // Also shows a snackbar with an undo action
    fun clearMessages() {
        val isChatEmpty = _activeChat.value.isEmpty() || _activeChat.value[0].text.isBlank()

        // Make a copy (backup) of the current chat before clearing it
        // if not empty
        if (isChatEmpty.not()) {
            _activeChatBackup.value = _activeChat.value
        }

        logger.log(TAG, "clearMessages()")
        while (_activeChat.value.isNotEmpty()) {
            deleteMessage(_activeChat.value.last().uuid)
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
        _activeChat.value = _activeChatBackup.value
    }

    // Replace all messages with a new list of messages
    fun setMessages(messages: List<Message>) {
        _activeChat.value = messages
    }

    private val _chatName = mutableStateOf("testChat")

    fun updateChatName(name: String) {
        _chatName.value = name
    }

    // Saves the current chat to the database
    @AddTrace(name = "saveChat", enabled = true)
    fun saveChat() {
        val personaSelected = viewModel.persona.selectedPersona.value
        val personaSystemMessage = viewModel.persona.personaSystemMessage.value
        val messages = mutableListOf<Message>()

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

        if (personaSystemMessage != "") {
            val systemMessage = Message(
                text = personaSystemMessage,
                role = Role.SYSTEM,
            )
            logger.logVerbose(TAG, "saveChat() systemMessage: $systemMessage")
            messages.add(systemMessage)
        }

        for (message in _activeChat.value) {
            logger.logVerbose(TAG, "saveChat() message: $message")
            if (message.text != "") {
                messages.add(message)
            }
        }

        viewModelScope.launch {
            chatService.saveChat(chat, messages)
            SnackbarManager.showMessage(R.string.chat_saved)
        }
    }

    companion object {
        private const val TAG = "ChatViewModel"
    }
}