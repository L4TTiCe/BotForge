// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.chat.headers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun CustomisePersonaHeader(
    modifier: Modifier = Modifier,
    showCommunityBadge: Boolean = false,
    onCommunityBadgeClick: () -> Unit = {},
    onEditPersonaClick: () -> Unit = {}
) {
    Row(modifier) {
        Column {
            Row {
                Text(
                    text = stringResource(id = R.string.customise_persona),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .padding(start = 10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                if (showCommunityBadge) {
                    IconButton(
                        onClick = onCommunityBadgeClick,
                        modifier = Modifier.padding()
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.community
                            ),
                            contentDescription = stringResource(id = R.string.community),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(id = R.string.create_persona_message),
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onEditPersonaClick,
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_drive_file_rename_outline_24
                ),
                contentDescription = stringResource(id = R.string.customise_persona),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Preview
@Composable
fun CustomisePersonaHeaderPreview() {
    CustomisePersonaHeader()
}

@Preview
@Composable
fun CustomisePersonaHeaderPreviewWithCommunityBadge() {
    CustomisePersonaHeader(
        showCommunityBadge = true,
    )
}

