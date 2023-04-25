package com.mohandass.botforge.image.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohandass.botforge.R

@Composable
fun DeleteAllGeneratedImagesDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.delete_all_images))
        },
        text = {
            Text(text = stringResource(id = R.string.delete_all_images_message))
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(
                    text = stringResource(id = R.string.delete),
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
    )
}

@Preview
@Composable
fun DeleteAllGeneratedImagesDialogPreview() {
    DeleteAllGeneratedImagesDialog(onDismiss = {}, onDelete = {})
}
