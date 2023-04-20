// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.ChatCard
import com.mohandass.botforge.chat.ui.components.ImageWithMessage
import com.mohandass.botforge.chat.ui.components.dialogs.DeleteHistoryDialog
import com.mohandass.botforge.chat.ui.components.header.HeaderWithActionIcon
import com.mohandass.botforge.chat.viewmodel.HistoryViewModel
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel


@Composable
fun HistoryUi(
    appViewModel: AppViewModel = hiltViewModel(),
    personaViewModel: PersonaViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val openDeleteDialog by historyViewModel.openDeleteHistoryDialog
    if (openDeleteDialog) {
        DeleteHistoryDialog(
            onDismiss = {
                historyViewModel.updateDeleteDialogState(false)
            },
            onConfirm = {
                historyViewModel.deleteAllChats()
                historyViewModel.updateDeleteDialogState(false)
            }
        )
    }


    val chats = historyViewModel.chats
    val personas = personaViewModel.personas

    LaunchedEffect(Unit) {
        personaViewModel.setChatType(ChatType.HISTORY)
        historyViewModel.fetchChats(onSuccess = {})
    }

    BackHandler {
        appViewModel.appState.navControllerPersona.popBackStack()
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
                    historyViewModel.updateDeleteDialogState(true)
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
                        persona = personas.value?.firstOrNull { it.uuid == chats[index].personaUuid },
                        getMessage = { onSuccess ->
                            historyViewModel.getMessagesCount(
                                chats[index].uuid,
                                onSuccess = onSuccess
                            )
                        },
                        onClick = {
                            historyViewModel.selectChat(chats[index])
                        },
                        onDelete = {
                            historyViewModel.deleteChat(chats[index].uuid)
                        },
                        onExport = {
                            historyViewModel.exportChat(chats[index].uuid, context)
                        },
                    )
                }
            }
        }
    }
}
