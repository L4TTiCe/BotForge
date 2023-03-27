package com.mohandass.botforge.chat.ui.components.messages

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.common.Utils
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageEntry(
    modifier: Modifier = Modifier,
    message: Message = Message(),
    viewModel: AppViewModel,
    startWithFocus: Boolean = false,
    startVisibility: Boolean = true,
//    scrollToItem: () -> Unit = {}
) {
    val showMetadata = remember { mutableStateOf(false) }
    val isActive = remember { mutableStateOf(message.isActive) }
    val showAllIcons = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    var messageContent by remember { mutableStateOf(message.text) }
    var role by remember { mutableStateOf(message.role) }

    val messageIsFocussed by viewModel.chat.isMessageInFocus

    val showMarkdownDialog = remember { mutableStateOf(false) }
    val showAsMarkdown = remember { mutableStateOf(true) }

    val cardColors = when (role) {
        Role.USER -> CardDefaults.cardColors()
        Role.BOT -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Role.SYSTEM -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        else -> {
            throw Exception("Invalid role")
        }
    }

    if (showMarkdownDialog.value) {
        AlertDialog(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                showMarkdownDialog.value = false
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = stringResource(R.string.markdown))
                }
            },
            text = {
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface,
                    markdown = messageContent
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showMarkdownDialog.value = false
                    },
                    content = {
                        Text(text = stringResource(R.string.dismiss))
                    }
                )
            }
        )
    }

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
        viewModel.chat.updateMessage(
            Message(
                text = messageContent,
                role = role,
                uuid = message.uuid,
                isActive = isActive.value,
                metadata = message.metadata
            )
        )
    }

    val roles by remember {
        mutableStateOf(listOf("User", "Bot", "System"))
    }

    val userColor = MaterialTheme.colorScheme.primary
    val botColor = MaterialTheme.colorScheme.tertiary
    val systemColor = MaterialTheme.colorScheme.secondary

    val colors by remember {
        mutableStateOf(listOf(userColor, botColor, systemColor))
    }

    val botTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        cursorColor = MaterialTheme.colorScheme.tertiary,
        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary
    )

    val systemTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        cursorColor = MaterialTheme.colorScheme.secondary,
        focusedIndicatorColor = MaterialTheme.colorScheme.secondary
    )

    // Visibility, used to animate the message entry and exit
    var visibility by remember { mutableStateOf(startVisibility) }

    LaunchedEffect(Unit) {
        visibility = true
    }

    fun handleDelete() {
        visibility = false

        // launch coroutine to delete message after animation
        viewModel.viewModelScope.launch {
            delay(500)
            viewModel.chat.deleteMessage(message.uuid)
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
                text = roles[when (role) {
                    Role.USER -> 0
                    Role.BOT -> 1
                    Role.SYSTEM -> 2
                    else -> {
                        throw Exception("Invalid role")
                    }
                }],
                modifier = modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                color = colors[when (role) {
                    Role.USER -> 0
                    Role.BOT -> 1
                    Role.SYSTEM -> 2
                    else -> {
                        throw Exception("Invalid role")
                    }
                }]
            )

            if (Utils.containsMarkdown(messageContent)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .padding(vertical = 8.dp)
                        .clickable {
                            showAsMarkdown.value = !showAsMarkdown.value
                        },
                    colors = cardColors,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showAsMarkdown.value) {
                            Text(text = stringResource(id = R.string.markdown_default))
                        } else {
                            Text(text = stringResource(id = R.string.markdown_switch))
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                            contentDescription = null
                        )
                    }
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
                    if (Utils.containsMarkdown(messageContent) && showAsMarkdown.value) {
                        Card(
                            modifier = modifier
                                .fillMaxWidth(),
                            colors = cardColors,
                            shape = MaterialTheme.shapes.small,
                        ) {
                            MarkdownText(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                color = when (role) {
                                    Role.USER -> MaterialTheme.colorScheme.onBackground
                                    Role.BOT -> MaterialTheme.colorScheme.onTertiaryContainer
                                    Role.SYSTEM -> MaterialTheme.colorScheme.onSecondaryContainer
                                    else -> {
                                        throw Exception("Invalid role")
                                    }
                                },
                                markdown = messageContent
                            )
                        }
                    } else {
                        TextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .sizeIn(minHeight = 100.dp),
                            value = messageContent,
                            onValueChange = {
                                messageContent = it
                                updateMessage()
                            },
                            enabled = isActive.value,
                            trailingIcon = {
                                IconButton(onClick = {
                                    role = Role.values()[(role.ordinal + 1) % 3]
                                    updateMessage()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.swap),
                                        modifier = Modifier.size(18.dp),
                                        contentDescription = null,
                                    )
                                }
                            },
                            colors = when (role) {
                                Role.USER -> TextFieldDefaults.textFieldColors()
                                Role.BOT -> botTextFieldColors
                                Role.SYSTEM -> systemTextFieldColors
                                else -> {
                                    throw Exception("Invalid role")
                                }
                            },
                        )
                    }
                    AnimatedVisibility(visible = showMetadata.value) {
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

                    // Delete button
                    IconButton(onClick = {
                        handleDelete()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }

                    val iconCount = 1 +
                            (if (message.metadata != null) 1 else 0) +
                            (if (message.text.isNotEmpty()) 1 else 0)

                    if (!showAllIcons.value && iconCount > 2) {
                        IconButton(onClick = {
                            showAllIcons.value = true
                            showMetadata.value = true
                        }) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = null
                            )
                        }
                    }

                    val icon =
                        if (isActive.value) R.drawable.show_eye else R.drawable.hide_eye

                    if (!showAllIcons.value &&
                        message.text.isNotEmpty() &&
                        iconCount <= 2
                    ) {
                        Column {
                            IconButton(onClick = {
                                isActive.value = !isActive.value
                                updateMessage()
                            }) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource(id = icon),
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = showAllIcons.value,
                        enter = slideInVertically(
                            initialOffsetY = { it }
                        ) + expandVertically(),
                        exit = slideOutVertically(
                            targetOffsetY = { -it + 120 }
                        ) + shrinkVertically()
                    ) {
                        Column {
                            IconButton(onClick = {
                                showMarkdownDialog.value = true
                            }) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                                    contentDescription = null
                                )
                            }

                            IconButton(onClick = {
                                showAllIcons.value = false
                                showMetadata.value = false
                            }) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                    contentDescription = null
                                )
                            }

                            if (message.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    isActive.value = !isActive.value
                                    updateMessage()
                                }) {
                                    Icon(
                                        modifier = Modifier.size(18.dp),
                                        painter = painterResource(id = icon),
                                        contentDescription = null
                                    )
                                }
                            }

                            if (message.metadata != null) {
                                // Metadata button
                                IconButton(onClick = {
                                    showMetadata.value = !showMetadata.value
                                }) {
                                    Icon(
                                        modifier = Modifier.size(18.dp),
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
