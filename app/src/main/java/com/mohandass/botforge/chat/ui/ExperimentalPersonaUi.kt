package com.mohandass.botforge.chat.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.header.AvatarsBar
import com.mohandass.botforge.chat.ui.components.chat.messages.MessageList
import com.mohandass.botforge.common.SnackbarManager
import com.slaviboy.composeunits.adh
import com.slaviboy.composeunits.adw

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ExperimentalPersonaUi(viewModel: AppViewModel) {

    val scrollState = rememberScrollState()
    val openDeleteDialog = remember { mutableStateOf(false) }
    val openAliasDialog = remember { mutableStateOf(false) }

    val hapticFeedback = LocalHapticFeedback.current

    val personaName by viewModel.persona.personaName
    val personaAlias by viewModel.persona.personaAlias
    val personaSystemMessage by viewModel.persona.personaSystemMessage
    val isLoading by viewModel.chat.isLoading

    if (openDeleteDialog.value) {
        AlertDialog(onDismissRequest = { openDeleteDialog.value = false },
            title = {
                Text(text = stringResource(id = R.string.delete_persona))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_persona_message))
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.persona.deletePersona()
                    openDeleteDialog.value = false
                }) {
                    Text(text = stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDeleteDialog.value = false
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

    /*TODO Extract string to string.xml*/
    if (openAliasDialog.value) {
        AlertDialog(onDismissRequest = { openAliasDialog.value = false },
            title = {
                Text(text = "Change Alias")
            },
            text = {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    OutlinedTextField(
                        value = personaAlias,
                        onValueChange = { viewModel.persona.updatePersonaAlias(it) },
                        label = { Text(text = "Alias") },
                    )

                    Spacer(modifier = Modifier.height(0.02.adh))

                    Text(text = "Alias is what is shown in the App's Top Bar / Header")

                    Spacer(modifier = Modifier.height(0.01.adh))

                    Text(text = "Try emoji's like 🤖")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.persona.saveUpdatePersona()
                    openAliasDialog.value = false
                }) {
                    Text(text = stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openAliasDialog.value = false
                }) {
                    Text(text = "Close")
                }
            }
        )
    }

    Column {
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                AvatarsBar(
                    viewModel = viewModel
                )

                Spacer(
                    modifier = Modifier.height(0.01.adh)
                )

                LinearProgressIndicator(
                    modifier = Modifier
                        .alpha(if (isLoading) 0.9f else 0f)
                        .fillMaxWidth(),
                )
            }
        }

        BottomSheetScaffold(
            sheetContent = {
                Scaffold(modifier = Modifier
                    .fillMaxSize(),
                    floatingActionButton = {
                        Column(
                            modifier = Modifier
                                .imePadding(),
                            horizontalAlignment = Alignment.End,
                        ) {
                            FloatingActionButton(
                                onClick = { viewModel.chat.handleDelete(true) },
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.baseline_clear_all_24
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            ExtendedFloatingActionButton(
                                onClick = {
                                    viewModel.chat.getChatCompletion {
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.TextHandleMove
                                        )
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.send),
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                        }
                    })
                {
                    Surface(
                        tonalElevation = 0.1.dp,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {

                            Text(
                                text = if (personaName != "")
                                    stringResource(
                                        id = R.string.chat_with_persona_name,
                                        personaName
                                    )
                                else
                                    stringResource(id = R.string.chat),
                                modifier = Modifier.padding(horizontal = 10.dp),
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(modifier = Modifier.height(0.02.adh))

                            MessageList(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                viewModel = viewModel
                            )

                            Spacer(modifier = Modifier.height(0.15.adh))

                        }
                    }

                }

            },
            sheetPeekHeight = 0.15.adh,
            sheetShape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
            sheetElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(10.dp)
                    .fillMaxSize()
            ) {

                Row {
                    Column {
                        Text(
                            text = stringResource(id = R.string.customise_persona),
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = stringResource(id = R.string.create_persona_message),
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { openAliasDialog.value = true },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.baseline_drive_file_rename_outline_24
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )
                    }


                }

                Spacer(modifier = Modifier.height(0.02.adh))

                OutlinedTextField(
                    value = personaName,
                    onValueChange = { viewModel.persona.updatePersonaName(it) },
                    label = {
                        Text(text = stringResource(id = R.string.persona_name))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxSize()
                )

                Spacer(modifier = Modifier.height(0.02.adh))

                Text(
                    text = stringResource(id = R.string.system_message),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                )

                OutlinedTextField(
                    value = personaSystemMessage,
                    onValueChange = { viewModel.persona.updatePersonaSystemMessage(it) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.system_message_hint)
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .sizeIn(minHeight = 0.2.adh)
                        .fillMaxSize()
                )

                Spacer(modifier = Modifier.height(0.02.adh))

                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    Button(
                        onClick = { /*TODO*/
                            SnackbarManager.showMessage(R.string.not_implemented)
                        },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(text = stringResource(id = R.string.share))
                    }

                    Button(
                        onClick = { viewModel.persona.saveUpdatePersona() },
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (viewModel.persona.selectedPersona.value != "") {
                        Button(
                            onClick = { openDeleteDialog.value = true },
                            modifier = Modifier.padding(horizontal = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(0.01.adw))
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }

                if (viewModel.persona.selectedPersona.value != "") {
                    Button(
                        onClick = { viewModel.persona.saveAsNewPersona() },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(text = stringResource(id = R.string.make_copy))
                    }
                }

                Spacer(modifier = Modifier.height(0.2.adh))
            }
        }
    }
}

