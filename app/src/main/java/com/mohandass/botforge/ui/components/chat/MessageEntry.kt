package com.mohandass.botforge.ui.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.R
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.ui.components.MessageMetadata
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.viewmodels.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageEntry(
    modifier: Modifier = Modifier,
    message: Message = Message(),
    viewModel: AppViewModel
) {
    val showMetadata = remember { mutableStateOf(false) }
    val isActive  = remember { mutableStateOf(message.isActive) }
    val showAllIcons = remember { mutableStateOf(false) }

    var messageContent by remember { mutableStateOf(message.text) }
    var role by remember { mutableStateOf(message.role) }

    fun updateMessage() {
        viewModel.updateMessage(
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
    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visibility = true
    }

    fun handleDelete() {
        visibility = false

        // launch coroutine to delete message after animation
        viewModel.viewModelScope.launch {
            delay(500)
            viewModel.deleteMessage(message.uuid)
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

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    TextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = 100.dp),
                        value = messageContent,
                        onValueChange = {
                            messageContent = it
                            updateMessage()
                        },
                        enabled = isActive.value,
                        trailingIcon = {
                            IconButton(onClick = { role = Role.values()[(role.ordinal + 1) % 3] }) {
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

                    AnimatedVisibility(visible = showMetadata.value) {
                        MessageMetadata(modifier = modifier, message = message)
                    }
                }

                Column (
                    modifier = modifier
                        .fillMaxHeight()
                ) {
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

                    AnimatedVisibility(visible = showAllIcons.value) {
                        Column {
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
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null
                                    )
                                }
                            }

                            if (showAllIcons.value) {
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
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageEntryPreview() {
    BotForgeTheme {
        MessageEntry(viewModel = hiltViewModel())
    }
}