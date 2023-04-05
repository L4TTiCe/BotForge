// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

// Serves as a send button and a cancel button
@Composable
fun SendFloatingActionButton(
    isLoading: Boolean,
    onSend: () -> Unit,
    onCancel: () -> Unit,
) {
    FloatingActionButton(
        onClick = {
            if (!isLoading) {
                onSend()
            } else {
                onCancel()
            }
        },
        containerColor =
        if (isLoading)
            MaterialTheme.colorScheme.errorContainer
        else
            MaterialTheme.colorScheme.primaryContainer,
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (!isLoading) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = stringResource(id = R.string.send),
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.cancel),
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun SendFloatingActionButtonPreview() {
    SendFloatingActionButton(
        isLoading = false,
        onSend = { },
        onCancel = { }
    )
}

@Preview
@Composable
fun SendFloatingActionButtonLoadingPreview() {
    SendFloatingActionButton(
        isLoading = true,
        onSend = { },
        onCancel = { }
    )
}
