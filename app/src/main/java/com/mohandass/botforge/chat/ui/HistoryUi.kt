// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.ChatCard
import com.mohandass.botforge.chat.ui.components.ImageWithMessage
import com.mohandass.botforge.chat.ui.components.dialogs.DeleteHistoryDialog
import com.mohandass.botforge.chat.ui.components.header.HeaderWithActionIcon

@Composable
fun HistoryUi(viewModel: AppViewModel) {

    val context = LocalContext.current

    val openDeleteDialog by viewModel.history.openDeleteHistoryDialog
    if (openDeleteDialog) {
        DeleteHistoryDialog(
            onDismiss = {
                viewModel.history.updateDeleteDialogState(false)
            },
            onConfirm = {
                viewModel.history.deleteAllChats()
                viewModel.history.updateDeleteDialogState(false)
            }
        )
    }


    val chats = viewModel.history.chats
    val personas = viewModel.persona.personas

    LaunchedEffect(Unit) {
        viewModel.persona.setChatType(ChatType.HISTORY)
        viewModel.history.fetchChats(onSuccess = {})
    }

    BackHandler {
        viewModel.persona.restoreState()
        viewModel.navControllerPersona.popBackStack()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        tonalElevation = 0.1.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {

            HeaderWithActionIcon(
                text = stringResource(id = R.string.bookmarked),
                leadingIcon = painterResource(id = R.drawable.baseline_bookmarks_24),
                leadingIconContentDescription = stringResource(id = R.string.bookmarks),
                trailingIcon = painterResource(id = R.drawable.baseline_clear_all_24),
                trailingIconContentDescription = stringResource(id = R.string.clear_all_cd),
                trailingIconOnClick = {
                    viewModel.history.updateDeleteDialogState(true)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Show empty box if no chats are bookmarked yet
            ImageWithMessage(
                visible = chats.isEmpty(),
                painter = painterResource(id = R.drawable.empty_box),
                imageContentDescription = stringResource(id = R.string.no_bookmarks_cd),
                message = stringResource(id = R.string.no_bookmarks),
            )

            // Show chats, if any, are bookmarked
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                // index chat item by uuid
                items(
                    count = chats.size,
                    key = { index -> chats[index].uuid }
                ) { index ->
                    // Each chat as a card
                    ChatCard(
                        chat = chats[index],
                        persona = personas.firstOrNull { it.uuid == chats[index].personaUuid },
                        getMessage = { onSuccess ->
                            viewModel.history.getMessagesCount(
                                chats[index].uuid,
                                onSuccess = onSuccess
                            )
                        },
                        onClick = {
                            viewModel.history.selectChat(chats[index])
                        },
                        onDelete = {
                            viewModel.history.deleteChat(chats[index].uuid)
                        },
                        onExport = {
                            viewModel.history.exportChat(chats[index].uuid, context)
                        },
                    )
                }
            }
        }
    }
}
