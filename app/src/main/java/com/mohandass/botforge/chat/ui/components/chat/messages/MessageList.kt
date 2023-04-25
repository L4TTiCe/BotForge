// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.chat.messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.viewmodel.ChatViewModel
import com.mohandass.botforge.common.Utils.Companion.formatDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val messagesList by chatViewModel.activeChat.collectAsState()
    val handleDelete by chatViewModel.handleDelete
    val messageIsFocussed by chatViewModel.isMessageInFocus

    val isLoading by chatViewModel.isLoading
    val timerMillis by chatViewModel.timeMillis

    val coroutineScope = rememberCoroutineScope()

    // Visibility,
    var visibility by remember { mutableStateOf(true) }

    fun handleClearAllMessages() {
        visibility = false

        chatViewModel.viewModelScope.launch {
            delay(600)
            chatViewModel.clearMessages()
            visibility = true
        }

        chatViewModel.handleDelete(false)
    }

    if (handleDelete) {
        handleClearAllMessages()
    }

    AnimatedVisibility(
        visible = visibility,
        enter = slideInHorizontally(initialOffsetX = { it - 200 }),
        exit = slideOutHorizontally(targetOffsetX = { it + 200 }),
    ) {
        Column(modifier = modifier) {
            messagesList.forEachIndexed { idx, item ->
                key(item.uuid) {
                    // use the unique identifier of each message as a key
                    MessageEntry(
                        modifier = Modifier,
                        message = item,
                        startWithFocus = (idx == (messagesList.size - 1)) && (idx != 0) && messageIsFocussed,
                        startVisibility = (idx != (messagesList.size - 1)) && item.text.isEmpty(),
                    )
                    Spacer(modifier = modifier.height(12.dp))
                }
            }

            if (isLoading) {
                Spacer(modifier = modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.88f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.bot),
                        modifier = Modifier,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )

                    TypingIndicator(modifier = Modifier.padding(horizontal = 12.dp))

                    Spacer(modifier = modifier.weight(1f))

                    Text(
                        text = formatDuration(timerMillis),
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    chatViewModel.isMessageInFocus.value = true
                    chatViewModel.autoAddMessage()
                    coroutineScope.launch {
                        delay(500)
                        chatViewModel.isMessageInFocus.value = false
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = stringResource(id = R.string.add_message_cd),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
