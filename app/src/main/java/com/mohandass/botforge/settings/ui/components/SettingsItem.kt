// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
// SPDX-FileCopyrightText: [2022 - Present] Stɑrry Shivɑm
//
// SPDX-License-Identifier: MIT

// Link to Source Repo:
// https://github.com/Pool-Of-Tears/GreenStash

package com.mohandass.botforge.settings.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun SettingsItem(title: String, description: String, painter: Painter, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 14.dp, end = 16.dp)
                    .size(32.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 8.dp)
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: Painter,
    switchState: MutableState<Boolean>,
    onCheckChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 14.dp, end = 16.dp)
                .size(26.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp)
        ) {
            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Switch(
            checked = switchState.value,
            onCheckedChange = { onCheckChange(it) },
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
    }
}

@Preview
@Composable
fun SettingsItemPreview() {
    SettingsItem(
        title = "Title",
        description = "Description",
        painter = painterResource(id = R.drawable.baseline_manage_accounts_24),
        onClick = {}
    )
}

@Preview
@Composable
fun SettingsItemSwitchPreview() {
    SettingsItem(
        title = "Title",
        description = "Description",
        icon = painterResource(id = R.drawable.baseline_manage_accounts_24),
        switchState = remember { mutableStateOf(true) },
        onCheckChange = {}
    )
}
