package com.mohandass.botforge.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.ChatCard
import com.mohandass.botforge.chat.ui.components.dialogs.DeleteHistoryDialog

@Composable
fun HistoryUi(viewModel: AppViewModel) {

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bookmarks_24),
                    contentDescription = stringResource(id = R.string.bookmarks),
                    modifier = Modifier
                )
                Text(
                    text = stringResource(id = R.string.bookmarked),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    viewModel.history.updateDeleteDialogState(true)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_clear_all_24),
                        contentDescription = stringResource(id = R.string.clear_all_cd),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (chats.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painterResource(id = R.drawable.empty_box),
                            modifier = Modifier
                                .size(150.dp)
                                .alpha(0.8f),
                            contentDescription = stringResource(id = R.string.no_bookmarks_cd)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(id = R.string.no_bookmarks),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                // index chat item by uuid
                items(
                    count = chats.size,
                    key = { index -> chats[index].uuid }
                ) { index ->
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
                        }
                    )
                }
            }
        }
    }
}
