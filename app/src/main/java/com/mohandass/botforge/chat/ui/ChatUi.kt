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
import com.mohandass.botforge.chat.ui.components.MessageList
import com.mohandass.botforge.chat.ui.components.RoundedIconFromStringAnimated
import com.mohandass.botforge.common.SnackbarManager
import com.slaviboy.composeunits.dh
import com.slaviboy.composeunits.dw

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatUi(viewModel: AppViewModel) {
    LaunchedEffect(Unit) {
        viewModel.topBar.title.value = R.string.app_name
        viewModel.topBar.overrideMenu.value = false
    }
    val listState = rememberLazyListState()

    val openDeleteDialog = remember { mutableStateOf(false) }
    val openAliasDialog = remember { mutableStateOf(false) }
    val openSaveChatDialog = remember { mutableStateOf(false) }

    val expandCustomizePersona = remember { mutableStateOf(false) }

    val hapticFeedback = LocalHapticFeedback.current

    val personaName by viewModel.persona.personaName
    val personaAlias by viewModel.persona.personaAlias
    val personaSystemMessage by viewModel.persona.personaSystemMessage
    val isLoading by viewModel.isLoading

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

    if (openSaveChatDialog.value) {
        val chatName = remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                chatName.value = ""
                openSaveChatDialog.value = false
           },
            title = {
                Text(text = "Name your Chat")
            },
            text = {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    OutlinedTextField(
                        value = chatName.value,
                        onValueChange = { chatName.value = it },
                        label = { Text(text = "Chat Name") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.chat.updateChatName(chatName.value)
                    viewModel.chat.saveChat()
                    openSaveChatDialog.value = false
                    chatName.value = ""
                }) {
                    Text(text = stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    chatName.value = ""
                    openSaveChatDialog.value = false
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

                    Row (
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
                        label = { Text(text = "Alias") },
                    )

                    Spacer(modifier = Modifier.height(0.02.dh))

                    Text(text = "Alias is what is shown in the App's Top Bar / Header")

                    Spacer(modifier = Modifier.height(0.01.dh))

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
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
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
//                    Spacer(modifier = Modifier.height(0.02.dh))

                    Row(
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
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (expandCustomizePersona.value) {
                            IconButton(
                                onClick = { expandCustomizePersona.value = false },
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.baseline_keyboard_arrow_up_24
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { expandCustomizePersona.value = true },
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.baseline_keyboard_arrow_down_24
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

//                    Spacer(modifier = Modifier.height(0.02.dh))
                }

                item {
                    AnimatedVisibility(
                        visible = expandCustomizePersona.value,
                        enter = slideInVertically {
                            -it - 150
                        } + expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically{
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

                            Spacer(modifier = Modifier.height(0.02.dh))

                            OutlinedTextField(
                                value = personaName,
                                onValueChange = { viewModel.persona.updatePersonaName(it) },
                                label = {
                                    Text(text = stringResource(id = R.string.persona_name))
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Text),
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(0.02.dh))

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
                                    .sizeIn(minHeight = 0.2.dh)
                                    .fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(0.02.dh))

                            Row(horizontalArrangement = Arrangement.SpaceAround) {
                                Button(
                                    onClick = { /*TODO*/
                                        SnackbarManager.showMessage(R.string.not_implemented)
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp)) {
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
                                        onClick = { openDeleteDialog.value = true },
                                        modifier = Modifier.padding(horizontal = 10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                                        Spacer(modifier = Modifier.width(0.01.dw))
                                        Text(text = stringResource(id = R.string.delete))
                                    }
                                }
                            }

                            if (viewModel.persona.selectedPersona.value != "") {
                                Button(
                                    onClick = { viewModel.persona.saveAsNewPersona() },
                                    modifier = Modifier.padding(horizontal = 10.dp)) {
                                    Text(text = stringResource(id = R.string.make_copy))
                                }
                            }

                            Spacer(modifier = Modifier.height(0.02.dh))
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
                    Row (
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
                            onClick = { openSaveChatDialog.value = true },
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.baseline_bookmark_add_24
                                ),
                                contentDescription = null,
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
                                contentDescription = null,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(0.01.dw))
                    }

                    Spacer(modifier = Modifier.height(0.02.dh))
                }

                item {
                    MessageList(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        viewModel = viewModel
                    )

                    Spacer(modifier = Modifier.height(0.2.dh))
                }
            }
        }
    }
}