package com.mohandass.botforge.ui.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
    var messageContent by remember { mutableStateOf(message.text) }
    var isUser by remember { mutableStateOf(message.role.isUser()) }

    val roles by remember {
        mutableStateOf(listOf("User", "Bot"))
    }

    val userColor = MaterialTheme.colorScheme.primary
    val botColor = MaterialTheme.colorScheme.tertiary

    val colors by remember {
        mutableStateOf(listOf(userColor, botColor))
    }

    val botTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        cursorColor = MaterialTheme.colorScheme.tertiary,
        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary
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
            viewModel.deleteMessage(
            Message(messageContent,
                if (isUser) Role.USER else Role.BOT, message.uuid))
        }
    }

    AnimatedVisibility(
        visible = visibility,
        enter = slideInHorizontally(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetX = { it + 200 }
        ),
        exit = slideOutHorizontally(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            targetOffsetX = { it + 200 }
        ),
    ) {
        Column {
            Text(
                text = roles[if (isUser) 0 else 1],
                modifier = modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = colors[if (isUser) 0 else 1]
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = messageContent,
                    onValueChange = {
                        messageContent = it
                        viewModel.updateMessage(Message(messageContent,
                            if (isUser) Role.USER else Role.BOT, message.uuid))
                    },
                    modifier = modifier.fillMaxWidth(0.85f),
                    trailingIcon = {
                        IconButton(onClick = { isUser = !isUser }) {
                            Icon(
                                painter = painterResource(id = R.drawable.swap),
                                modifier = Modifier.size(18.dp),
                                contentDescription = null,
                            )
                        }
                    },
                    colors = if (isUser) TextFieldDefaults.textFieldColors() else botTextFieldColors,
                )

                // Delete button
                IconButton(onClick = {
                    handleDelete()
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
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