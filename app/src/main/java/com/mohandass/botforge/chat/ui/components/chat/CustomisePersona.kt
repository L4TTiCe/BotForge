// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.slaviboy.composeunits.adh
import com.slaviboy.composeunits.dw

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomisePersona(
    modifier: Modifier = Modifier,
    personaName: String,
    personaSystemMessage: String,
    hasSelectedPersona: Boolean,
    onPersonaNameChange: (String) -> Unit,
    onPersonaSystemMessageChange: (String) -> Unit,
    onShare: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
) {
    Column(modifier) {
        OutlinedTextField(
            value = personaName,
            onValueChange = onPersonaNameChange,
            label = {
                Text(text = stringResource(id = R.string.persona_name))
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(0.02.adh))

        Text(
            text = stringResource(id = R.string.system_message),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.labelLarge,
        )

        OutlinedTextField(
            value = personaSystemMessage,
            onValueChange = onPersonaSystemMessageChange,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.system_message_hint)
                )
            },
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .sizeIn(minHeight = 0.2.adh)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(0.02.adh))

        // Buttons
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            // Share Button
            Button(
                onClick = onShare,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.share))
            }

            // Save Button
            Button(
                onClick = onSave,
            ) {
                Text(text = stringResource(id = R.string.save))
            }

            Spacer(modifier = Modifier.weight(1f))

            // Delete Button
            if (hasSelectedPersona) {
                Button(
                    onClick = onDelete,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete)
                    )
                    Spacer(modifier = Modifier.width(0.01.dw))
                    Text(text = stringResource(id = R.string.delete))
                }
            }
        }

        // Make Copy Button
        if (hasSelectedPersona) {
            Button(
                onClick = onCopy,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.make_copy))
            }
        }

        Spacer(modifier = Modifier.height(0.02.adh))
    }
}

@Preview(showBackground = true)
@Composable
fun CustomisePersonaPreview() {
    var personaName by remember { mutableStateOf("Persona Name") }
    var personaSystemMessage by remember { mutableStateOf("System Message") }

    CustomisePersona(
        personaName = personaName,
        personaSystemMessage = personaSystemMessage,
        hasSelectedPersona = true,
        onPersonaNameChange = {
            personaName = it
        },
        onPersonaSystemMessageChange = {
            personaSystemMessage = it
        },
        onShare = {},
        onSave = {},
        onDelete = {},
        onCopy = {},
    )
}

@Preview(showBackground = true)
@Composable
fun CustomisePersonaPreviewOnUnSavedPersona() {
    var personaName by remember { mutableStateOf("Persona Name") }
    var personaSystemMessage by remember { mutableStateOf("System Message") }

    CustomisePersona(
        personaName = personaName,
        personaSystemMessage = personaSystemMessage,
        hasSelectedPersona = false,
        onPersonaNameChange = {
            personaName = it
        },
        onPersonaSystemMessageChange = {
            personaSystemMessage = it
        },
        onShare = {},
        onSave = {},
        onDelete = {},
        onCopy = {},
    )
}
