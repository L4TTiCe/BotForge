// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.mohandass.botforge.R
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveChatDialog(
    initialChatName: String = "",
    generatedChatName: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    generateChatName: KFunction1<(String) -> Unit, Unit>
) {
    val chatName = remember { mutableStateOf(initialChatName) }
    val generatedName = remember { mutableStateOf(generatedChatName) }

    LaunchedEffect(Unit) {
        generateChatName {
            generatedName.value = it

            if (chatName.value.isEmpty() || chatName.value.isBlank()) {
                chatName.value = it
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.save_chat_dialog_title))
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save_chat_dialog_chat_title_hint),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = generatedName.value,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            chatName.value = generatedName.value
                        }
                        .placeholder(
                            visible = generatedName.value.isEmpty() ||
                                    generatedName.value.isBlank(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = chatName.value,
                    onValueChange = { chatName.value = it },
                    label = {
                        Text(text = stringResource(id = R.string.save_chat_dialog_chat_title))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(chatName.value) }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                TextButton(onClick = { chatName.value = "" }) {
                    Text(text = stringResource(id = R.string.clear))
                }
            }
        }
    )
}

@Preview
@Composable
fun SavePersonaDialogPreview() {
    SaveChatDialog(
        initialChatName = "",
        onDismiss = {},
        onConfirm = {},
        generateChatName = ::println
    )
}
