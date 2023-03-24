package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R

@Composable
fun DeletePersonaDialog(viewModel: AppViewModel) {
    val openDeleteDialog by viewModel.persona.openDeleteDialog

    if (openDeleteDialog) {
        AlertDialog(onDismissRequest = { viewModel.persona.updateDeletePersonaDialogState(false) },
            title = {
                Text(text = stringResource(id = R.string.delete_persona))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_persona_message))
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.persona.deletePersona()
                    viewModel.persona.updateDeletePersonaDialogState(false)
                }) {
                    Text(text = stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.persona.updateDeletePersonaDialogState(false)
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}