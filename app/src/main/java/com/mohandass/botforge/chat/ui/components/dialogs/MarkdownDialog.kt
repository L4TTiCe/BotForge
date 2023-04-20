// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Samples
import dev.jeziellago.compose.markdowntext.MarkdownText

// Renders markdown in a dialog
@Composable
fun MarkdownDialog(
    markdown: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = stringResource(R.string.markdown))
            }
        },
        text = {
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                markdown = markdown
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.dismiss))
                }
            )
        }
    )
}

@Preview
@Composable
fun MarkdownDialogPreview() {
    MarkdownDialog(
        markdown = Samples.MARKDOWN,
        onDismiss = {}
    )
}
