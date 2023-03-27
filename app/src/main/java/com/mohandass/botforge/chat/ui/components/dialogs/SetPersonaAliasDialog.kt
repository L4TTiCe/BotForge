package com.mohandass.botforge.chat.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromStringAnimated
import com.slaviboy.composeunits.dh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPersonaAliasDialog(viewModel: AppViewModel) {
    val openAliasDialog by viewModel.chat.openAliasDialog

    val personaName by viewModel.persona.personaName
    val personaAlias by viewModel.persona.personaAlias

    if (openAliasDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.chat.updateAliasDialogState(false)
            },
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
                            text = (personaAlias.ifEmpty { personaName }),
                            modifier = Modifier.size(90.dp),
                            onClick = {}
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    OutlinedTextField(
                        value = personaAlias,
                        onValueChange = { viewModel.persona.updatePersonaAlias(it) },
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
                    viewModel.persona.saveUpdatePersona()
                    viewModel.chat.updateAliasDialogState(false)
                }) {
                    Text(text = stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.chat.updateAliasDialogState(false)
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}
