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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.chat.ui.components.chat.CustomisePersona
import com.mohandass.botforge.chat.ui.components.chat.SendFloatingActionButton
import com.mohandass.botforge.chat.ui.components.chat.headers.CustomisePersonaHeader
import com.mohandass.botforge.chat.ui.components.chat.headers.MessagesHeader
import com.mohandass.botforge.chat.ui.components.chat.headers.PersonaChatHeader
import com.mohandass.botforge.chat.ui.components.chat.messages.MessageList
import com.mohandass.botforge.chat.ui.components.dialogs.DeletePersonaDialog
import com.mohandass.botforge.chat.ui.components.dialogs.SavePersonaDialog
import com.mohandass.botforge.chat.ui.components.dialogs.SetPersonaAliasDialog
import com.mohandass.botforge.sync.ui.components.BotDetailDialog
import com.slaviboy.composeunits.adh

// Main Chat UI, with Customise Persona and Messages
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatUi(viewModel: AppViewModel) {
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val expandCustomizePersona by viewModel.persona.expandCustomizePersona

    val hapticFeedback = LocalHapticFeedback.current

    val personaName by viewModel.persona.personaName
    val personaSystemMessage by viewModel.persona.personaSystemMessage
    val isLoading by viewModel.chat.isLoading
    val parentBot by viewModel.persona.parentBot

    val openDeleteDialog by viewModel.persona.openDeleteDialog
    val openSaveChatDialog by viewModel.chat.openSaveChatDialog
    val openAliasDialog by viewModel.chat.openAliasDialog
    val showDetailDialog = remember { mutableStateOf(false) }

    var isUserGeneratedContentEnabled by remember {
        mutableStateOf(false)
    }

    val userPreferences by viewModel.userPreferences.observeAsState()
    userPreferences?.let {
        isUserGeneratedContentEnabled = it.enableUserGeneratedContent
    }

    // Show bot detail dialog for Bots from Community
    if (showDetailDialog.value) {
        parentBot?.let {
            BotDetailDialog(
                it,
                showAdd = false,
                onClickDismiss = { showDetailDialog.value = false },
                onClickAccept = {
                    showDetailDialog.value = false
                },
                onUpVote = {
                    viewModel.browse.upVote(it.uuid)
                },
                onDownVote = { viewModel.browse.downVote(it.uuid) },
                onReport = { viewModel.browse.report(it.uuid) },
            )
        }
    }

    // Confirm delete dialog
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

    // Asks for chat name to save
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

    // Sets alias for persona
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
            // Sends or Cancels request
            SendFloatingActionButton(
                isLoading = isLoading,
                onSend = {
                    viewModel.chat.getChatCompletion {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onCancel = {
                    viewModel.chat.handleInterrupt()
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
                                viewModel.persona.updateExpandCustomizePersona(false)
                            } else {
                                viewModel.persona.updateExpandCustomizePersona(true)
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
                                    viewModel.chat.updateAliasDialogState(true)
                                },
                            )

                            Spacer(modifier = Modifier.height(0.02.adh))

                            CustomisePersona(
                                personaName = personaName,
                                personaSystemMessage = personaSystemMessage,
                                hasSelectedPersona = viewModel.persona.selectedPersona.value != "",
                                onPersonaNameChange = { viewModel.persona.updatePersonaName(it) },
                                onPersonaSystemMessageChange = {
                                    viewModel.persona.updatePersonaSystemMessage(it)
                                },
                                onShare = { viewModel.persona.showSharePersona() },
                                onSave = { viewModel.persona.saveUpdatePersona() },
                                onDelete = {
                                    viewModel.persona.updateDeletePersonaDialogState(true)
                                },
                                onCopy = { viewModel.persona.saveAsNewPersona() },
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
                        onBookmarkClick = {
                            viewModel.chat.updateSaveChatDialogState(true)
                        },
                        onClearAllClick = {
                            viewModel.chat.handleDelete(true)
                        }
                    )

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
