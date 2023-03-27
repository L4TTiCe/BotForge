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
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromString
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@Composable
fun ChatCard(
    chat: Chat,
    persona: Persona?,
    initialMessageCount: Int = 0,
    getMessage: ((Int) -> Unit) -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    var messageCount by remember { mutableStateOf(initialMessageCount) }

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
                    Icon(
                        painter = painterResource(id = com.mohandass.botforge.R.drawable.logo),
                        contentDescription = "Person",
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
                    Column {
                        Text(
                            text = chat.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (persona != null) {
                            Text(
                                text = (persona.name),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            if (isDeleted) {
                                Text(
                                    text = "Deleted Persona",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = "Default Persona",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = onDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
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

                    Text(
                        text = messageCount.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Icon(
                        painter = painterResource(id = com.mohandass.botforge.R.drawable.baseline_message_24),
                        contentDescription = "Message",
                        modifier = Modifier
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

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
