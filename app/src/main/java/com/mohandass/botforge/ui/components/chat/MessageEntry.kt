package com.mohandass.botforge.ui.components

import androidx.compose.foundation.clickable
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
import com.mohandass.botforge.R
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageEntry(
    modifier: Modifier = Modifier,
    message: Message = Message(),
    viewModel: AppViewModel = hiltViewModel()
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

    Column {
        Text(
            text = roles[if (isUser) 0 else 1],
            modifier = modifier.fillMaxWidth()
                .clickable {
                    isUser = !isUser
                    viewModel.updateMessage(Message(messageContent,
                        if (isUser) Role.USER else Role.BOT, message.uuid))
                },
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
                viewModel.deleteMessage(
                    Message(messageContent,
                    if (isUser) Role.USER else Role.BOT, message.uuid))
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }

    }


}

@Preview(showBackground = true)
@Composable
fun MessageEntryPreview() {
    BotForgeTheme {
        MessageEntry()
    }
}