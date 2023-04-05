// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.chat.messages

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

data class MessageEntrySideIconsState(
    val isActive: Boolean,
    val hasText: Boolean,
    val hasMetadata: Boolean,
)

@Composable
fun MessageEntrySideIcons(
    messageEntrySideIconsState: MessageEntrySideIconsState,
    onDelete: () -> Unit,
    onShowMarkdown: () -> Unit,
    onToggleActive: () -> Unit,
    onToggleMetadata: () -> Unit,
    onExpand: () -> Unit,
    onShrink: () -> Unit,
) {
    var showAllIcons by remember { mutableStateOf(false) }
    val hasText = messageEntrySideIconsState.hasText
    val hasMetadata = messageEntrySideIconsState.hasMetadata
    val isActive = messageEntrySideIconsState.isActive

    Column {
        // Delete button
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete),
            )
        }

        val isOverflowing = hasText && hasMetadata

        if (!showAllIcons && isOverflowing) {
            IconButton(onClick = {
                showAllIcons = true
                onExpand()
            }) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = stringResource(id = R.string.show_more_cd)
                )
            }
        }

        if (!showAllIcons &&
            hasText &&
            isOverflowing.not()
        ) {
            Column {
                ToggleActiveMessageButton(isActive = isActive, onToggleActive = onToggleActive)
            }
        }

        AnimatedVisibility(
            visible = showAllIcons,
            enter = slideInVertically(
                initialOffsetY = { it }
            ) + expandVertically(),
            exit = slideOutVertically(
                targetOffsetY = { -it + 120 }
            ) + shrinkVertically()
        ) {
            Column {

                // Icon to Show Markdown Dialog
                IconButton(onClick = onShowMarkdown) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                        contentDescription = stringResource(id = R.string.show_as_md_cd)
                    )
                }

                // Show Less
                IconButton(onClick = {
                    showAllIcons = false
                    onShrink()
                }) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                        contentDescription = stringResource(id = R.string.show_less_cd)
                    )
                }

                if (hasText) {
                    ToggleActiveMessageButton(isActive = isActive, onToggleActive = onToggleActive)
                }

                // Show / Hide Metadata
                if (hasMetadata) {
                    // Metadata button
                    IconButton(onClick = onToggleMetadata) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(id = R.string.info_cd)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToggleActiveMessageButton(
    isActive: Boolean,
    onToggleActive: () -> Unit,
) {
    val icon =
        if (isActive) R.drawable.show_eye else R.drawable.hide_eye

    IconButton(onClick = onToggleActive) {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = icon),
            contentDescription = if (isActive) {
                stringResource(id = R.string.set_message_inactive_cd)
            } else {
                stringResource(id = R.string.set_message_active_cd)
            }
        )
    }
}

@Preview
@Composable
fun MessageEntrySideIconsPreview() {
    var isActive by remember { mutableStateOf(true) }

    MessageEntrySideIcons(
        messageEntrySideIconsState = MessageEntrySideIconsState(
            isActive = isActive,
            hasText = true,
            hasMetadata = true,
        ),
        onDelete = {},
        onShowMarkdown = {},
        onToggleActive = {
            isActive = !isActive
        },
        onToggleMetadata = {},
        onExpand = {},
        onShrink = {},
    )
}
