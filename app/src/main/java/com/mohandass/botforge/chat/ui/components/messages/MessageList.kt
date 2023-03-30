package com.mohandass.botforge.chat.ui.components.messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MessageList(modifier: Modifier = Modifier, viewModel: AppViewModel) {
    val messagesList by viewModel.chat.activeChat
    val handleDelete by viewModel.chat.handleDelete
    val messageIsFocussed by viewModel.chat.isMessageInFocus

    val isLoading by viewModel.isLoading

    val coroutineScope = rememberCoroutineScope()

    // Visibility,
    var visibility by remember { mutableStateOf(true) }

    fun handleClearAllMessages() {
        visibility = false

        viewModel.viewModelScope.launch {
            delay(600)
            viewModel.chat.clearMessages()
            visibility = true
        }

        viewModel.chat.handleDelete(false)
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
                        viewModel = viewModel,
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
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.bot),
                        modifier = Modifier,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )

                    TypingIndicator(modifier = Modifier.padding(horizontal = 12.dp))
                }
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    viewModel.chat.isMessageInFocus.value = true
                    viewModel.chat.autoAddMessage()
                    coroutineScope.launch {
                        delay(500)
                        viewModel.chat.isMessageInFocus.value = false
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
