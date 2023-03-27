package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromStringAnimated
import com.slaviboy.composeunits.dh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPersonaAliasDialog(
    initialPersonaAlias: String = "",
    personaName: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var alias by remember { mutableStateOf(initialPersonaAlias) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.set_alias))
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    RoundedIconFromStringAnimated(
                        text = (alias.ifEmpty { personaName }),
                        modifier = Modifier.size(90.dp),
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }

                OutlinedTextField(
                    value = alias,
                    onValueChange = { alias = it },
                    label = {
                        Text(text = stringResource(id = R.string.alias))
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.robot),
                            contentDescription = null
                        )
                    },
                )

                Spacer(modifier = Modifier.height(0.02.dh))

                Text(text = stringResource(id = R.string.set_alias_message))

                Spacer(modifier = Modifier.height(0.01.dh))

                Text(text = stringResource(id = R.string.set_alias_message_2))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(alias)
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun SetPersonaAliasDialogPreview() {
    SetPersonaAliasDialog(
        initialPersonaAlias = "",
        personaName = "Celsius",
        onDismiss = {},
        onConfirm = {}
    )
}
