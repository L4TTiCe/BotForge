package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavePersonaDialog(viewModel: AppViewModel) {
    val openSaveChatDialog by viewModel.chat.openSaveChatDialog

    if (openSaveChatDialog) {
        val chatName = remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                chatName.value = ""
                viewModel.chat.updateSaveChatDialogState(false)
            },
            title = {
                Text(text = "Name your Chat")
            },
            text = {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    OutlinedTextField(
                        value = chatName.value,
                        onValueChange = { chatName.value = it },
                        label = { Text(text = "Chat Name") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.chat.updateChatName(chatName.value)
                    viewModel.chat.saveChat()
                    viewModel.chat.updateSaveChatDialogState(false)
                    chatName.value = ""
                }) {
                    Text(text = stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    chatName.value = ""
                    viewModel.chat.updateSaveChatDialogState(false)
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}