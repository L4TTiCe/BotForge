package com.mohandass.botforge.chat.ui

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.dialogs.DeletePersonaDialog
import com.mohandass.botforge.chat.ui.components.dialogs.SavePersonaDialog
import com.mohandass.botforge.chat.ui.components.dialogs.SetPersonaAliasDialog
import com.mohandass.botforge.chat.ui.components.messages.MessageList
import com.slaviboy.composeunits.adh
import com.slaviboy.composeunits.dw

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatUi(viewModel: AppViewModel) {
    val listState = rememberLazyListState()

    val expandCustomizePersona by viewModel.persona.expandCustomizePersona

    val hapticFeedback = LocalHapticFeedback.current

    val personaName by viewModel.persona.personaName
    val personaSystemMessage by viewModel.persona.personaSystemMessage
    val isLoading by viewModel.chat.isLoading

    val openDeleteDialog by viewModel.persona.openDeleteDialog
    val openSaveChatDialog by viewModel.chat.openSaveChatDialog
    val openAliasDialog by viewModel.chat.openAliasDialog

    if (openDeleteDialog) {
        DeletePersonaDialog(
            onDismiss = {
                viewModel.persona.updateDeletePersonaDialogState(false)
            },
            onConfirm = {
                viewModel.persona.deletePersona()
                viewModel.persona.updateDeletePersonaDialogState(false)
            })
    }

    if (openSaveChatDialog) {
        SavePersonaDialog(
            onDismiss = {
                viewModel.chat.updateSaveChatDialogState(false)
            },
            onConfirm = {
                viewModel.chat.updateChatName(it)
                viewModel.chat.saveChat()
                viewModel.chat.updateSaveChatDialogState(false)
            }
        )
    }

    if (openAliasDialog) {
        SetPersonaAliasDialog(
            onDismiss = {
                viewModel.chat.updateAliasDialogState(false)
            },
            onConfirm = {
                viewModel.persona.updatePersonaAlias(it)
                viewModel.persona.saveUpdatePersona()
                viewModel.chat.updateAliasDialogState(false)
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isLoading) {
                        viewModel.chat.getChatCompletion(hapticFeedback)
                    } else {
                        viewModel.chat.handleInterrupt()
                    }
                },
                containerColor =
                if (isLoading)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (!isLoading) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = stringResource(id = R.string.send),
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.cancel),
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        },
    ) {
        Surface(
            tonalElevation = 0.1.dp,
            modifier = Modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                state = listState,
            ) {
                item {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = if (personaName != "")
                                stringResource(
                                    id = R.string.chat_with_persona_name,
                                    personaName
                                )
                            else
                                stringResource(id = R.string.chat),
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .fillMaxWidth(0.85f),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                if (expandCustomizePersona) {
                                    viewModel.persona.updateExpandCustomizePersona(false)
                                } else {
                                    viewModel.persona.updateExpandCustomizePersona(true)
                                }
                            },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (expandCustomizePersona)
                                        R.drawable.baseline_keyboard_arrow_up_24
                                    else
                                        R.drawable.baseline_keyboard_arrow_down_24
                                ),
                                contentDescription = stringResource(id =
                                if (expandCustomizePersona)
                                    R.string.show_less_cd
                                else
                                    R.string.show_more_cd),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = expandCustomizePersona,
                        enter = slideInVertically {
                            -it - 150
                        } + expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically {
                            -it - 150
                        } + shrinkVertically() + fadeOut(
                            targetAlpha = 0f
                        )
                    ) {
                        Column {
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
                                    onClick = {
                                        viewModel.chat.updateAliasDialogState(true)
                                    },
                                    modifier = Modifier.padding(10.dp)
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
                                onValueChange = { viewModel.persona.updatePersonaSystemMessage(it) },
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

                            Row(horizontalArrangement = Arrangement.SpaceAround) {
                                Button(
                                    onClick = {
                                        viewModel.persona.showSharePersona()
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                ) {
                                    Text(text = stringResource(id = R.string.share))
                                }

                                Button(
                                    onClick = {
                                        viewModel.persona.saveUpdatePersona()
                                    },
                                ) {
                                    Text(text = stringResource(id = R.string.save))
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                if (viewModel.persona.selectedPersona.value != "") {
                                    Button(
                                        onClick = {
                                            viewModel.persona.updateDeletePersonaDialogState(true)
                                        },
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

                            if (viewModel.persona.selectedPersona.value != "") {
                                Button(
                                    onClick = { viewModel.persona.saveAsNewPersona() },
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                ) {
                                    Text(text = stringResource(id = R.string.make_copy))
                                }
                            }

                            Spacer(modifier = Modifier.height(0.02.adh))
                        }
                    }
                }

                item {
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Messages",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                viewModel.chat.updateSaveChatDialogState(true)
                            },
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.baseline_bookmark_add_24
                                ),
                                contentDescription = stringResource(id = R.string.add_to_bookmarks_cd),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.chat.handleDelete(true) },
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.baseline_clear_all_24
                                ),
                                contentDescription = stringResource(id = R.string.clear_all_cd),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(0.01.dw))
                    }

                    Spacer(modifier = Modifier.height(0.02.adh))
                }

                item {
                    MessageList(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        viewModel = viewModel
                    )

                    Spacer(modifier = Modifier.height(0.2.adh))
                }
            }
        }
    }
}
