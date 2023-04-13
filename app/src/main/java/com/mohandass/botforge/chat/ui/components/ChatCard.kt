// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromString
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

// Displays a chat card with the persona icon, number of messages, and time since saved
@Composable
fun ChatCard(
    chat: Chat,
    persona: Persona?,
    initialMessageCount: Int = 0,
    getMessage: ((Int) -> Unit) -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onExport: () -> Unit,
) {
    var messageCount by remember { mutableStateOf(initialMessageCount) }

    // Check if the Persona used in the chat is deleted
    val isDeleted by remember {
        if (chat.personaUuid == null) {
            mutableStateOf(false)
        } else {
            if (persona == null) {
                mutableStateOf(true)
            } else {
                mutableStateOf(false)
            }
        }
    }

    val time by remember {
        val timestamp = chat.savedAt
        val date = Date(timestamp)
        val prettyTime = PrettyTime()
        prettyTime.locale = Locale.getDefault()
        mutableStateOf(prettyTime.format(date))
    }

    LaunchedEffect(Unit) {
        try {
            getMessage { messageCount = it }
        } catch (_: Exception) {
            // Do nothing
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(5.dp),
//                        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row {
            Column {
                if (persona != null) {
                    RoundedIconFromString(
                        text = (
                                if (persona.alias != "")
                                    persona.alias
                                else
                                    persona.name
                                ),
                        modifier = Modifier.size(90.dp),
                        borderColor = Color.Transparent,
                        onClick = { }
                    )
                } else {
                    // Deleted Persona or Default Persona
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(id = R.string.default_persona),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(80.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            Column(
                modifier = Modifier.padding(10.dp),
            ) {

                Row {
                    // Chat name and persona name
                    Column {
                        Text(
                            text = chat.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        // Determine if the Persona used in the chat is available, deleted,
                        // or default
                        if (persona != null) {
                            // Persona is available
                            Text(
                                text = (persona.name),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            if (isDeleted) {
                                // Persona is deleted
                                Text(
                                    text = stringResource(id = R.string.deleted_persona),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                // Default Persona
                                Text(
                                    text = stringResource(id = R.string.default_persona),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Delete button
                    IconButton(
                        onClick = onDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete),
                            modifier = Modifier
                                .size(20.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Message count
                    Text(
                        text = messageCount.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_message_24),
                        contentDescription = stringResource(id = R.string.messages),
                        modifier = Modifier
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = onExport) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_document_scanner_24),
                            contentDescription = stringResource(id = R.string.export_chat_cd),
                            modifier = Modifier
                                .size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Time since saved
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = time.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatCardPreview() {
    ChatCard(
        chat = Chat(
            name = "Chat",
            savedAt = 1620000000000,
            personaUuid = null
        ),
        persona = null,
        initialMessageCount = 12,
        getMessage = {},
        onClick = {},
        onDelete = {},
        onExport = {}
    )
}
