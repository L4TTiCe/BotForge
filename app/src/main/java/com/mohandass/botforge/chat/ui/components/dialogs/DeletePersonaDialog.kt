package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohandass.botforge.R

@Composable
fun DeletePersonaDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest =  onDismiss,
        title = {
            Text(text = stringResource(id = R.string.delete_persona))
        },
        text = {
            Text(text = stringResource(id = R.string.delete_persona_message))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.delete))
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
fun DeletePersonaDialogPreview() {
    DeletePersonaDialog(
        onDismiss = {},
        onConfirm = {}
    )
}