// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.chat.CustomisePersona
import com.mohandass.botforge.chat.ui.components.chat.SendFloatingActionButton
import com.mohandass.botforge.chat.ui.components.chat.headers.CustomisePersonaHeader
import com.mohandass.botforge.chat.ui.components.chat.headers.PersonaChatHeader
import com.mohandass.botforge.chat.ui.components.chat.messages.MessageList
import com.mohandass.botforge.chat.ui.components.dialogs.DeletePersonaDialog
import com.mohandass.botforge.chat.ui.components.dialogs.SaveChatDialog
import com.mohandass.botforge.chat.ui.components.dialogs.SetPersonaAliasDialog
import com.mohandass.botforge.chat.ui.components.header.MessagesHeader
import com.mohandass.botforge.chat.viewmodel.ChatViewModel
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.common.services.snackbar.SnackbarLauncherLocation
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.services.snackbar.SwipeableSnackbarHost
import com.mohandass.botforge.rememberSnackbarLauncher
import com.mohandass.botforge.sync.ui.components.BotDetailDialog
import com.mohandass.botforge.sync.ui.components.BotDetailDialogConfig
import com.mohandass.botforge.sync.viewmodel.BrowseViewModel
import com.slaviboy.composeunits.adh

// Main Chat UI, with Customise Persona and Messages
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatUi(
    appViewModel: AppViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    personaViewModel: PersonaViewModel = hiltViewModel(),
    browseViewModel: BrowseViewModel = hiltViewModel(),
) {
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val hapticFeedback = LocalHapticFeedback.current

    val expandCustomizePersona by personaViewModel.expandCustomizePersona

    val personaUuid by personaViewModel.personaUuid.collectAsState()
    val personaName by personaViewModel.personaName.collectAsState()
    val personaSystemMessage by personaViewModel.personaSystemMessage.collectAsState()
    val isLoading by chatViewModel.isLoading
    val parentBot by personaViewModel.parentBot.collectAsState()

    val openDeleteDialog by personaViewModel.openDeleteDialog
    val openSaveChatDialog by chatViewModel.openSaveChatDialog
    val openAliasDialog by chatViewModel.openAliasDialog
    val showDetailDialog = remember { mutableStateOf(false) }

    var isUserGeneratedContentEnabled by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (personaUuid == "") {
            personaViewModel.setChatType(ChatType.CREATE)
        } else {
            personaViewModel.setChatType(ChatType.CHAT)
        }
    }

    val userPreferences by appViewModel.appState.userPreferences.observeAsState()
    userPreferences?.let {
        isUserGeneratedContentEnabled = it.enableUserGeneratedContent
    }

    // Show bot detail dialog for Bots from Community
    if (showDetailDialog.value) {
        parentBot?.let {
            val botDetailDialogConfig = BotDetailDialogConfig(
                bot = it,
                onUpVote = {
                    browseViewModel.upVote(it.uuid)
                },
                onDownVote = { browseViewModel.downVote(it.uuid) },
                onReport = { browseViewModel.report(it.uuid) },
            )

            BotDetailDialog(
                showAdd = false,
                onClickDismiss = { showDetailDialog.value = false },
                onClickAccept = {
                    showDetailDialog.value = false
                },
                config = botDetailDialogConfig
            )
        }
    }

    // Confirm delete dialog
    if (openDeleteDialog) {
        DeletePersonaDialog(
            onDismiss = {
                personaViewModel.updateDeletePersonaDialogState(false)
            },
            onConfirm = {
                personaViewModel.deletePersona()
                personaViewModel.updateDeletePersonaDialogState(false)
            })
    }

    // Asks for chat name to save
    if (openSaveChatDialog) {
        SaveChatDialog(
            onDismiss = {
                chatViewModel.updateSaveChatDialogState(false)
            },
            onConfirm = {
                chatViewModel.updateChatName(it)
                chatViewModel.saveChat()
                chatViewModel.updateSaveChatDialogState(false)
            },
            isAutoGenerateChatNameEnabled = userPreferences?.autoGenerateChatTitle ?: true,
            generateChatName = chatViewModel::generateChatName
        )
    }

    // Sets alias for persona
    if (openAliasDialog) {
        SetPersonaAliasDialog(
            onDismiss = {
                chatViewModel.updateAliasDialogState(false)
            },
            onConfirm = {
                personaViewModel.updatePersonaAlias(it)
                personaViewModel.saveUpdatePersona()
                chatViewModel.updateAliasDialogState(false)
            }
        )
    }

    DisposableEffect(Unit) {
        appViewModel.appState.setActiveSnackbar(SnackbarLauncherLocation.CHAT)

        onDispose {
            appViewModel.appState.setActiveSnackbar(SnackbarLauncherLocation.MAIN)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    rememberSnackbarLauncher(
        snackbarHostState,
        snackbarManager = SnackbarManager.getInstance(SnackbarLauncherLocation.CHAT)
    )

    Scaffold(
        snackbarHost = { SwipeableSnackbarHost(snackbarHostState) },
        floatingActionButton = {
            // Sends or Cancels request
            SendFloatingActionButton(
                isLoading = isLoading,
                onSend = {
                    chatViewModel.getChatCompletion {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onCancel = {
                    chatViewModel.handleInterrupt()
                }
            )
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
                    PersonaChatHeader(
                        personaName = personaName,
                        expandCustomizePersona = expandCustomizePersona,
                        onExpandOrCollapse = {
                            if (expandCustomizePersona) {
                                personaViewModel.updateExpandCustomizePersona(false)
                            } else {
                                personaViewModel.updateExpandCustomizePersona(true)
                            }
                        }
                    )
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
                            CustomisePersonaHeader(
                                showCommunityBadge =
                                parentBot != null && isUserGeneratedContentEnabled,
                                onCommunityBadgeClick = {
                                    showDetailDialog.value = true
                                },
                                onEditPersonaClick = {
                                    chatViewModel.updateAliasDialogState(true)
                                },
                            )

                            Spacer(modifier = Modifier.height(0.02.adh))

                            // Customise Persona
                            CustomisePersona(
                                personaName = personaName,
                                personaSystemMessage = personaSystemMessage,
                                hasSelectedPersona = personaUuid != "",
                                onPersonaNameChange = { personaViewModel.updatePersonaName(it) },
                                onPersonaSystemMessageChange = {
                                    personaViewModel.updatePersonaSystemMessage(it)
                                },
                                onShare = { personaViewModel.showSharePersona() },
                                onSave = { personaViewModel.saveUpdatePersona() },
                                onDelete = {
                                    personaViewModel.updateDeletePersonaDialogState(true)
                                },
                                onCopy = { personaViewModel.saveAsNewPersona() },
                            )
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
                    MessagesHeader(
                        onPdfExport = {
                            chatViewModel.exportAsPdf(context)
                        },
                        onExportClick = {
                            chatViewModel.exportChatAsJson(context)
                        },
                        onBookmarkClick = {
                            chatViewModel.updateSaveChatDialogState(true)
                        },
                        onClearAllClick = {
                            chatViewModel.handleDelete(true)
                        }
                    )

                    Spacer(modifier = Modifier.height(0.02.adh))
                }

                item {
                    MessageList(
                        modifier = Modifier
                            .padding(start = 10.dp),
                    )

                    Spacer(modifier = Modifier.height(0.2.adh))
                }
            }
        }
    }
}
