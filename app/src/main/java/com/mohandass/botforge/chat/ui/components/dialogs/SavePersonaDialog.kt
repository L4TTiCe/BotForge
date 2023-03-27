package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavePersonaDialog(
    initialChatName: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val chatName = remember { mutableStateOf(initialChatName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.save_persona_dialog_title))
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    value = chatName.value,
                    onValueChange = { chatName.value = it },
                    label = {
                        Text(text = stringResource(id = R.string.save_persona_dialog_chat_title))
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
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun SavePersonaDialogPreview() {
    SavePersonaDialog(
        initialChatName = "",
        onDismiss = {},
        onConfirm = {}
    )
}
