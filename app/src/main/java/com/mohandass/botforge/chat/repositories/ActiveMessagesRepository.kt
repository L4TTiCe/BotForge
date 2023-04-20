// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.repositories

import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/*
 * A Repository to hold the active chat
 *
 * This is used to pass the active chat between screens, and to
 * update the active chat
 */
class ActiveMessagesRepository (
    private val appState: AppState,
    private val activePersonaRepository: ActivePersonaRepository
) {
    private val _activeChat = MutableStateFlow<List<Message>>(emptyList())
    val activeChat: StateFlow<List<Message>> = _activeChat

    init {
        addMessage(Message())
    }

    fun addMessage(message: Message) {
        _activeChat.value = _activeChat.value + message
    }

    fun updateActiveChat(chat: List<Message>) {
        _activeChat.value = chat
    }

    fun clear() {
        _activeChat.value = emptyList()
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
        _activeChat.value = _activeChat.value.filter {
            it.uuid != messageUuid
        }
    }

    // Replace all messages with a new list of messages
    fun setMessages(messages: List<Message>) {
        _activeChat.value = messages
    }

    fun getAllMessages(
        includeSystemMessage: Boolean,
        includeInactiveMessages: Boolean
    ): List<Message> {
        val messages = mutableListOf<Message>()

        if (includeSystemMessage) {
            val personaSystemMessage = activePersonaRepository.activePersonaSystemMessage.value

            if (personaSystemMessage == "") {
                val systemMessage = Message(
                    text = appState.resources.getString(R.string.system_message_default),
                    role = Role.SYSTEM
                )
                messages.add(systemMessage)
            } else {
                val systemMessage = Message(
                    text = personaSystemMessage,
                    role = Role.SYSTEM
                )
                messages.add(systemMessage)
            }
        }

        for (message in _activeChat.value) {
            if (message.text != "") {
                if (includeInactiveMessages) {
                    messages.add(message)
                } else {
                    if (message.isActive) {
                        messages.add(message)
                    }
                }
            }
        }

        return messages
    }
}
