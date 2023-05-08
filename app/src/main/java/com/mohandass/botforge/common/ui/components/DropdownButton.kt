// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T> DropdownButton(
    modifier: Modifier = Modifier,
    options: List<T>,
    selectedOption: String,
    onOptionSelected: (T) -> Unit,
) {
    var showList by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            modifier = modifier.fillMaxWidth(),
            onClick = { showList = !showList }
        ) {
            Text(selectedOption)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = showList,
            onDismissRequest = { showList = false },
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        showList = false
                        onOptionSelected(item)
                    },
                    text = { Text(item.toString()) }
                )
            }
        }
    }
}