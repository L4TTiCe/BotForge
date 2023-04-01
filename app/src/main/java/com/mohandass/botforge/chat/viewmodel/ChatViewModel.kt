package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.SnackbarMessage.Companion.toSnackbarMessageWithAction
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

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

    private fun interruptRequest() {
        if (_requestInProgress.value) {
            _requestInProgress.value = false
        }
        if (this::job.isInitialized) {
            job.cancel()
        }
        viewModel.setLoading(false)
    }

    fun handleInterrupt() {
        SnackbarManager.showMessageWithAction(R.string.waiting_for_response, R.string.cancel) {
            interruptRequest()
        }
    }

    @AddTrace(name = "getChatCompletion", enabled = true)
    fun getChatCompletion(hapticFeedback: HapticFeedback) {
        logger.log(TAG, "getChatCompletion()")
        viewModel.setLoading(true)

        val messages = mutableListOf<Message>()
        val personaSystemMessage = viewModel.persona.personaSystemMessage.value

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
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.setLoading(false)
        }
    }

    // Message

    private val _activeChat = mutableStateOf(listOf(Message()))
    val activeChat: MutableState<List<Message>> = _activeChat

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

    fun clearMessages() {
        logger.log(TAG, "clearMessages()")
        while (_activeChat.value.isNotEmpty()) {
            deleteMessage(_activeChat.value.last().uuid)
        }

        autoAddMessage()
    }

    fun setMessages(messages: List<Message>) {
        _activeChat.value = messages
    }

    private val _chatName = mutableStateOf("testChat")

    fun updateChatName(name: String) {
        _chatName.value = name
    }

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