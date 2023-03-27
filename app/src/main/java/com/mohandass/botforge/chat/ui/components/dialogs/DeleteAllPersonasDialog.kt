package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohandass.botforge.R

@Composable
fun DeleteAllPersonasDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.delete_all_persona),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.delete_all_persona_message),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(id = R.string.delete_all),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.errorContainer,
        textContentColor = MaterialTheme.colorScheme.onErrorContainer,
    )
}

@Preview
@Composable
fun DeleteAllPersonasDialogPreview() {
    DeleteAllPersonasDialog(
        onDismiss = { },
        onConfirm = { }
    )
}
