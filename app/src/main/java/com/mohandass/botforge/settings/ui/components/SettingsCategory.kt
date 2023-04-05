// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
// SPDX-FileCopyrightText: [2022 - Present] Stɑrry Shivɑm
//
// SPDX-License-Identifier: MIT

// Link to Source Repo:
// https://github.com/Pool-Of-Tears/GreenStash

package com.mohandass.botforge.settings.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsCategory(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(horizontal = 14.dp)
    )
}

@Preview
@Composable
fun SettingsCategoryPreview() {
    SettingsCategory(title = "Category")
}
