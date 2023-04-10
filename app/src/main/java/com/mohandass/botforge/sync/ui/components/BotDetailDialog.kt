// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromStringAnimated
import com.mohandass.botforge.common.ui.theme.BotForgeLightThemePreview
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.dao.entities.BotEProvider
import com.slaviboy.composeunits.adh
import java.util.*

data class BotDetailDialogConfig(
    val bot: BotE,
    val onUpVote: () -> Unit,
    val onDownVote: () -> Unit,
    val onReport: () -> Unit,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BotDetailDialog(
    showAdd: Boolean = true,
    onClickDismiss: () -> Unit,
    onClickAccept: () -> Unit,
    config: BotDetailDialogConfig,
) {
    val bot = config.bot

    AlertDialog(
        onDismissRequest = onClickDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .heightIn(max = 0.9.adh)
            .padding(24.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = bot.name)

                Spacer(modifier = Modifier.weight(1f))

                RoundedIconFromStringAnimated(
                    text = (
                            if (bot.alias != "")
                                bot.alias
                            else
                                bot.name
                            ),
                    // Offset the icon to the top right corner
                    modifier = Modifier
                        .size(90.dp),
                    onClick = { }
                )
            }
        },
        text = {
            LazyColumn {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
//                        .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.by),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = bot.createdBy,
                            modifier = Modifier
                                .padding(2.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                }

                item {
                    Text(
                        text = stringResource(id = R.string.description),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = bot.description,
                        modifier = Modifier
                            .fillMaxWidth(),
//                        .background(
//                            color = MaterialTheme.colorScheme.background
//                        )
//                        .border(
//                            width = 1.dp,
//                            color = MaterialTheme.colorScheme.background,
//                            shape = MaterialTheme.shapes.extraSmall
//                        )
//                        .padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Text(
                        text = stringResource(id = R.string.system_message),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = bot.systemMessage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.background,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (bot.tags.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.tags),
                            style = MaterialTheme.typography.labelMedium,
                        )

                        LazyRow {
                            items(bot.tags.size) { tag ->
                                Chip(
                                    onClick = { },
                                    modifier = Modifier.padding(horizontal = 2.dp),
                                    colors = ChipDefaults.chipColors(
                                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                ) {
                                    Text(
                                        text = bot.tags[tag],
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = bot.userUpVotes.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                IconButton(onClick = config.onUpVote) {
                    Icon(
                        painter = painterResource(id = R.drawable.upvote_filled),
                        contentDescription = stringResource(id = R.string.up_vote),
                        modifier = Modifier
                            .size(18.dp)
                    )
                }

                IconButton(onClick = config.onDownVote) {
                    Icon(
                        painter = painterResource(id = R.drawable.downvote_filled),
                        contentDescription = stringResource(id = R.string.down_vote),
                        modifier = Modifier
                            .size(18.dp)
                    )
                }

                Text(
                    text = bot.userDownVotes.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                if (showAdd) {
                    TextButton(onClick = onClickAccept) {
                        Text(
                            text = stringResource(id = R.string.add),
                        )
                    }
                } else {
                    TextButton(onClick = onClickDismiss) {
                        Text(
                            text = stringResource(id = R.string.dismiss),
                        )
                    }
                }
            }

        },
        dismissButton = {
            // Report button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = config.onReport,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.report),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
    )
}

@Preview
@Composable
fun BotDetailDialogPreview(
    @PreviewParameter(BotEProvider::class) bot: BotE,
) {
    val config = BotDetailDialogConfig(
        bot = bot,
        onUpVote = {},
        onDownVote = {},
        onReport = {},
    )

    BotForgeLightThemePreview {
        BotDetailDialog(showAdd = true, {}, {}, config)
    }
}
