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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.chat.ui.components.chat.messages.markdown.MarkdownCard
import com.mohandass.botforge.chat.ui.components.chat.messages.markdown.MarkdownInfoCard
import com.mohandass.botforge.chat.ui.components.dialogs.MarkdownDialog
import com.mohandass.botforge.chat.viewmodel.ChatViewModel
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.ui.ShakeWithHaptic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MessageEntry(
    modifier: Modifier = Modifier,
    message: Message = Message(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    appViewModel: AppViewModel = hiltViewModel(),
    startWithFocus: Boolean = false,
    startVisibility: Boolean = true,
//    scrollToItem: () -> Unit = {}
) {
    var showMetadata by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(message.isActive) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    var removedMessageContent by remember { mutableStateOf("") }
    var messageContent by remember { mutableStateOf(TextFieldValue(text = message.text)) }
    var role by remember { mutableStateOf(message.role) }

    val messageIsFocussed by chatViewModel.isMessageInFocus

    var showMarkdownDialog by remember { mutableStateOf(false) }
    var showAsMarkdown by remember { mutableStateOf(true) }

    var isShakeToClearEnabled by remember {
        mutableStateOf(false)
    }
    var shakeSensitivity by remember {
        mutableStateOf(0f)
    }

    val userPreferences = appViewModel.appState.userPreferences.observeAsState()
    userPreferences.value?.let {
        isShakeToClearEnabled = it.enableShakeToClear
        shakeSensitivity = it.shakeToClearSensitivity
    }

    if (showMarkdownDialog) {
        MarkdownDialog(
            markdown = messageContent.text,
            onDismiss = {
                showMarkdownDialog = false
            },
        )
    }

    // Request focus, if the message is active and empty and the message is focussed previously
    LaunchedEffect(Unit) {
        delay(200)
        if (
            message.isActive &&
            message.text.isEmpty() &&
            message.role == Role.USER &&
            startWithFocus &&
            messageIsFocussed
        ) {
//            scrollToItem()
            delay(200)
            focusRequester.requestFocus()
        }
    }

    fun updateMessage() {
        chatViewModel.updateMessage(
            Message(
                text = messageContent.text,
                role = role,
                uuid = message.uuid,
                isActive = isActive,
                metadata = message.metadata
            )
        )
    }

    // Visibility, used to animate the message entry and exit
    var visibility by remember { mutableStateOf(startVisibility) }

    LaunchedEffect(Unit) {
        visibility = true
    }

    fun handleDelete() {
        visibility = false

        // launch coroutine to delete message after animation
        chatViewModel.viewModelScope.launch {
            delay(500)
            chatViewModel.deleteMessage(message.uuid)
        }
    }

    AnimatedVisibility(
        visible = visibility,
        enter = slideInHorizontally(
//            animationSpec = spring(
//                dampingRatio = Spring.DampingRatioLowBouncy,
//                stiffness = Spring.StiffnessLow
//            ),
            initialOffsetX = { it + 200 }
        ),
        exit = slideOutHorizontally(
//            animationSpec = spring(
//                dampingRatio = Spring.DampingRatioLowBouncy,
//                stiffness = Spring.StiffnessLow
//            ),
            targetOffsetX = { it + 200 }
        ),
    ) {
        Column {
            Text(
                text = role.asString(),
                modifier = modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                color = role.labelColor(),
            )

            // Card to inform that Content is Markdown and can be shown as such
            // Allows to switch between Markdown and plain text
            // Markdown is shown by default
            // Markdown does not allow editing, or selecting text
            if (Utils.containsMarkdown(messageContent.text)) {
                MarkdownInfoCard(
                    modifier = modifier
                        .fillMaxWidth(0.88f),
                    isShownAsMarkdown = showAsMarkdown,
                    cardColors = role.cardColors()
                ) {
                    showAsMarkdown = !showAsMarkdown
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.88f)
                ) {
                    val renderAsMarkdown = Utils.containsMarkdown(messageContent.text)
                            && showAsMarkdown

                    // Show Markdown as Markdown
                    MarkdownCard(
                        modifier = modifier
                            .fillMaxWidth(),
                        markdown = messageContent.text,
                        role = role,
                        isVisible = renderAsMarkdown
                    )

                    // Shake to Clear
                    ShakeWithHaptic(
                        shakeSensitivity = shakeSensitivity,
                        isEnabled = isShakeToClearEnabled && isFocused && !renderAsMarkdown,
                    ) {
                        removedMessageContent = messageContent.text
                        messageContent = TextFieldValue()

                        SnackbarManager.showMessageWithAction(
                            message = R.string.message_cleared,
                            dismissLabel = R.string.undo,
                            dismissAction = {
                                messageContent = TextFieldValue(removedMessageContent)

                                // Move Cursor to end of text
                                messageContent = TextFieldValue(
                                    text = messageContent.text,
                                    selection = TextRange(messageContent.text.length)
                                )
                            }
                        )
                    }

                    // Text field to enter message
                    MessageEntryField(
                        modifier = modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            },
                        isActive = isActive,
                        value = messageContent,
                        onValueChange = {
                            messageContent = it
                            updateMessage()
                        },
                        role = role,
                        onRoleChange = {
                            role = Role.values()[(role.ordinal + 1) % 3]
                            updateMessage()
                        },
                        isVisible = !renderAsMarkdown
                    )

                    AnimatedVisibility(visible = showMetadata) {
                        MessageMetadata(modifier = modifier, message = message)
                    }
                }

                Column(
                    modifier = modifier
                        .weight(0.12f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Make all elements stick to the bottom
                    Spacer(modifier = Modifier.weight(1f))

                    // Icons to toggle active, delete, show markdown, show metadata
                    MessageEntrySideIcons(
                        messageEntrySideIconsState = MessageEntrySideIconsState(
                            isActive = isActive,
                            hasText = message.text.isNotEmpty(),
                            hasMetadata = message.metadata != null
                        ),
                        onDelete = { handleDelete() },
                        onShowMarkdown = { showMarkdownDialog = true },
                        onToggleActive = {
                            isActive = !isActive
                            updateMessage()
                        },
                        onToggleMetadata = {
                            showMetadata = !showMetadata
                        },
                        onExpand = {
                            showMetadata = true
                        },
                        onShrink = {
                            showMetadata = false
                        }
                    )
                }
            }
        }
    }
}
