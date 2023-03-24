package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R

@Composable
fun DeleteHistoryDialog(viewModel: AppViewModel) {
    val openDeleteDialog by viewModel.history.openDeleteHistoryDialog

    if (openDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.history.updateDeleteDialogState(false)
            },
            title = {
                Text(text = stringResource(id = R.string.delete_all_bookmarked))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_all_bookmarked_message))
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.history.deleteAllChats()
                    viewModel.history.updateDeleteDialogState(false)
                }) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.history.updateDeleteDialogState(false)
                }) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.errorContainer,
        )
    }
}
