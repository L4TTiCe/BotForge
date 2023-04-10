// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.ImageWithMessage
import com.mohandass.botforge.chat.ui.components.PersonaInfo
import com.mohandass.botforge.chat.ui.components.dialogs.DeleteAllPersonasDialog
import com.mohandass.botforge.chat.ui.components.header.HeaderWithActionIcon
import com.mohandass.botforge.sync.ui.components.BotDetailDialogConfig
import com.slaviboy.composeunits.adh

@Composable
fun PersonaListUi(
    viewModel: AppViewModel
) {
    val personaListViewModel = viewModel.personaList

    var showDeleteAllPersonaDialog by personaListViewModel.showDeleteAllPersonaDialog
    val personas = personaListViewModel.personas

    if (showDeleteAllPersonaDialog) {
        DeleteAllPersonasDialog(
            onDismiss = {showDeleteAllPersonaDialog = false},
            onConfirm = {
                personaListViewModel.deleteAllPersonas()
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.persona.setChatType(ChatType.LIST)
        personaListViewModel.fetchBots()
    }

    BackHandler {
        personaListViewModel.onBack()
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
                text = stringResource(id = R.string.personas),
                leadingIcon = painterResource(id = R.drawable.list),
                trailingIcon = painterResource(id = R.drawable.baseline_clear_all_24),
                trailingIconContentDescription = stringResource(id = R.string.clear_all_cd),
                trailingIconOnClick = {
                    showDeleteAllPersonaDialog = true
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ImageWithMessage(
                visible = personas.isEmpty(),
                painter = painterResource(id = R.drawable.empty_box),
                message = stringResource(id = R.string.no_personas_yet),
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                items(
                    count = personas.size,
                    key = { index -> personas[index].uuid }
                ) { index ->
                    var botDetailDialogConfig: BotDetailDialogConfig? = null

                    if (personas[index].parentUuid != "") {
                        val bot = personaListViewModel.getBot(personas[index].parentUuid)

                        botDetailDialogConfig = bot?.let {
                            BotDetailDialogConfig(
                                bot = it,
                                onUpVote = {
                                    viewModel.browse.upVote(bot.uuid)
                                },
                                onDownVote = { viewModel.browse.downVote(bot.uuid) },
                                onReport = { viewModel.browse.report(bot.uuid) }
                            )
                        }
                    }

                    PersonaInfo(
                        persona = personas[index],
                        onClick = {
                            viewModel.persona.selectPersona(personas[index].uuid)
                        },
                        onClickDelete = {
                            personaListViewModel.deletePersona(personas[index].uuid)
                        },
                        botDetailDialogConfig = botDetailDialogConfig
                    )

                    Divider()

                }

                item {
                    Spacer(modifier = Modifier.height(0.2.adh))
                }
            }
        }
    }
}
