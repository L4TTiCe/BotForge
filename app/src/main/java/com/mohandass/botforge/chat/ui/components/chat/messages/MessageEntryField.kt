// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.chat.messages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Role

// A text field that allows the user to enter a message.
// Uses different colors depending on the role (User, Bot, or System).
// The trailing icon allows the user to swap the role, for this message.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageEntryField(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    role: Role,
    onRoleChange: () -> Unit,
    isVisible: Boolean
) {
    if (!isVisible) return

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 100.dp),
        value = value,
        onValueChange = onValueChange,
        enabled = isActive,
        trailingIcon = {
            IconButton(
                onClick = onRoleChange
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.swap),
                    modifier = Modifier.size(20.dp),
                    contentDescription = stringResource(id = R.string.swap_cd),
                )
            }
        },
        colors = role.textFieldColors(),
    )
}

@Preview
@Composable
fun MessageEntryFieldPreviewEnabled() {
    var role by remember { mutableStateOf(Role.USER) }
    var messageContent by remember { mutableStateOf(TextFieldValue(text = "")) }

    MessageEntryField(
        isActive = true,
        value = messageContent,
        onValueChange = {
            messageContent = it
        },
        role = role,
        onRoleChange = {
            role = Role.values()[(role.ordinal + 1) % 3]
        },
        isVisible = true
    )
}

@Preview
@Composable
fun MessageEntryFieldPreviewDisabled() {
    var role by remember { mutableStateOf(Role.USER) }
    var messageContent by remember { mutableStateOf(TextFieldValue(text = "")) }

    MessageEntryField(
        isActive = false,
        value = messageContent,
        onValueChange = {
            messageContent = it
        },
        role = role,
        onRoleChange = {
            role = Role.values()[(role.ordinal + 1) % 3]
        },
        isVisible = true
    )
}
