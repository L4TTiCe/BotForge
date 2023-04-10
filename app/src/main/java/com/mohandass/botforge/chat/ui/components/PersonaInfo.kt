// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components

import androidx.compose.animation.*
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.dao.entities.Persona
import com.mohandass.botforge.chat.model.dao.entities.PersonaProvider
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromString
import com.mohandass.botforge.common.ui.theme.BotForgeLightThemePreview
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.ui.components.BotDetailDialog
import com.mohandass.botforge.sync.ui.components.BotDetailDialogConfig
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@Composable
fun PersonaInfo(
    modifier: Modifier = Modifier,
    persona: Persona,
    onClickDelete: () -> Unit,
    botDetailDialogConfig: BotDetailDialogConfig? = null,
    onClick: () -> Unit = {}
) {

    var showDetailDialog by remember {
        mutableStateOf(false)
    }

    val time by remember {
        val timestamp = persona.lastUsed
        val date = Date(timestamp)
        val prettyTime = PrettyTime()
        prettyTime.locale = Locale.getDefault()
        mutableStateOf(prettyTime.format(date))
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val hasOverflow = textLayoutResult.value?.hasVisualOverflow ?: false
    val isEllipsized = 
        try {
            textLayoutResult.value?.isLineEllipsized(1) ?: false
        } catch (e: Exception) {
            false
        }

    if (botDetailDialogConfig != null && showDetailDialog) {
        BotDetailDialog(
            showAdd = false,
            config = botDetailDialogConfig,
            onClickDismiss = { showDetailDialog = false },
            onClickAccept = {
                showDetailDialog = false
            },
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column {
            RoundedIconFromString(
                text = (
                        if (persona.alias != "")
                            persona.alias
                        else
                            persona.name
                        ),
                modifier = Modifier.size(90.dp),
                borderColor = Color.Transparent,
                onClick = onClick
            )
        }

        Column(
            modifier = Modifier.padding(10.dp),
        ) {

            Row {
                // Chat name and persona name
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    Row {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClick() },
                            text = persona.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = stringResource(id = R.string.system_message),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text (
                        modifier = Modifier
                            .animateContentSize()
                            .clickable {
                                isExpanded = !isExpanded
                            },
                        text = persona.systemMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = {
                            if (textLayoutResult.value == null)
                                textLayoutResult.value = it
                        },
                    )
                }

                Column {
                    if (persona.parentUuid.isNotBlank()) {
                        IconButton(
                            onClick = {
                                showDetailDialog = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.community),
                                contentDescription = stringResource(id = R.string.community),
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                    }

                    // Delete button
                    IconButton(
                        onClick = onClickDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete),
                            modifier = Modifier
                                .size(20.dp),
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // last used
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = time.toString(),
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.weight(1f))
                if (hasOverflow || isEllipsized) {

                    Text(
                        modifier = Modifier
                            .clickable {
                                isExpanded = !isExpanded
                            },
                        text = if (isExpanded) "Show Less" else "Show More",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    IconButton(
                        modifier = Modifier.padding(0.dp),
                        onClick = {
                            isExpanded = !isExpanded
                        }) {
                        val icon =
                            if (isExpanded)
                                R.drawable.baseline_keyboard_arrow_up_24
                            else
                                R.drawable.baseline_keyboard_arrow_down_24

                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(0.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonaInfoPreview(
    @PreviewParameter(PersonaProvider::class) persona: Persona
) {
    BotForgeLightThemePreview {
        val config = BotDetailDialogConfig(
            bot = BotE.sampleBot1,
            onUpVote = {},
            onDownVote = {},
            onReport = {},
        )

        PersonaInfo(
            persona = persona,
            onClickDelete = {},
            botDetailDialogConfig = config
        )
    }
}
