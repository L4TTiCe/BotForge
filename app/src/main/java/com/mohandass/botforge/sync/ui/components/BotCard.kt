// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromString
import com.mohandass.botforge.common.ui.theme.BotForgeDarkThemePreview
import com.mohandass.botforge.common.ui.theme.BotForgeLightThemePreview
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.dao.entities.BotEProvider
import com.slaviboy.composeunits.dw

@Composable
fun BotCard(
    botE: BotE,
    onClickButton: () -> Unit,
    onUpVote: () -> Unit,
    onDownVote: () -> Unit,
    onReport: () -> Unit,
) {
    val showDetailDialog = remember { mutableStateOf(false) }

    if (showDetailDialog.value) {
        val botDetailDialogConfig = BotDetailDialogConfig(
            bot = botE,
            onUpVote = onUpVote,
            onDownVote = onDownVote,
            onReport = onReport,
        )

        BotDetailDialog(
            showAdd = true,
            onClickDismiss = { showDetailDialog.value = false },
            onClickAccept = {
                onClickButton()
                showDetailDialog.value = false
            },
            config = botDetailDialogConfig
        )
    }

    Card(
        modifier = Modifier
            .padding(5.dp)
            .width(
                if (0.9.dw > 400.dp)
                    400.dp
                else
                    0.9.dw
            )
            .clickable {
                showDetailDialog.value = true
            },
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                RoundedIconFromString(
                    text = (
                            if (botE.alias != "")
                                botE.alias
                            else
                                botE.name
                            ),
                    modifier = Modifier.size(90.dp),
                    borderColor = Color.Transparent,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(3f)
            ) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = botE.name,
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = stringResource(id = R.string.by) + " " + botE.createdBy,
                    style = MaterialTheme.typography.bodySmall,
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = botE.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onClickButton) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cloud_download_24),
                        contentDescription = stringResource(id = R.string.add),
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun BotCardPreviewLight(
    @PreviewParameter(BotEProvider::class) bot: BotE,
) {
    BotForgeLightThemePreview {
        BotCard(bot, {}, {}, {}, {})
    }
}

@Preview
@Composable
fun BotCardPreviewDark(
    @PreviewParameter(BotEProvider::class) bot: BotE,
) {
    BotForgeDarkThemePreview {
        BotCard(bot, {}, {}, {}, {})
    }
}
