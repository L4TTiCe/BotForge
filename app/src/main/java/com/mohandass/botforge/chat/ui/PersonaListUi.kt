// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.ImageWithMessage
import com.mohandass.botforge.chat.ui.components.PersonaInfo
import com.mohandass.botforge.chat.ui.components.dialogs.DeleteAllPersonasDialog
import com.mohandass.botforge.chat.ui.components.header.HeaderWithActionIcon
import com.mohandass.botforge.common.ui.components.NoMatches
import com.mohandass.botforge.common.ui.components.SearchBar
import com.mohandass.botforge.sync.ui.components.BotDetailDialogConfig
import com.slaviboy.composeunits.adh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaListUi(
    viewModel: AppViewModel
) {
    val personaListViewModel = viewModel.personaList

    val personas by viewModel.persona.personas.observeAsState(initial = emptyList())
    val matchedPersonas = personaListViewModel.matchedPersonas

    var showDeleteAllPersonaDialog by personaListViewModel.showDeleteAllPersonaDialog
    val searchQuery by personaListViewModel.searchQuery

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
        Column{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {
                    HeaderWithActionIcon(
                        modifier = Modifier
                            .padding(10.dp),
                        text = stringResource(id = R.string.personas),
                        leadingIcon = painterResource(id = R.drawable.list),
                        trailingIcon = painterResource(id = R.drawable.baseline_clear_all_24),
                        trailingIconContentDescription = stringResource(id = R.string.clear_all_cd),
                        trailingIconOnClick = {
                            showDeleteAllPersonaDialog = true
                        }
                    )
                }

                item {
                    if (personas.isNotEmpty()) {
                        return@item
                    }
                    Spacer(modifier = Modifier.height(0.15.adh))
                    ImageWithMessage(
                        visible = personas.isEmpty(),
                        painter = painterResource(id = R.drawable.empty_box),
                        message = stringResource(id = R.string.no_personas_yet),
                    )
                }

                item {

                    if (personas.isEmpty()) {
                        return@item
                    }

                    SearchBar(
                        searchQuery = searchQuery,
                        onClear = {
                            personaListViewModel.updateSearchQuery("")
                        },
                        label = stringResource(id = R.string.search_personas),
                    ) {
                        personaListViewModel.updateSearchQuery(it)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item{
                    if (searchQuery == "") {
                        return@item
                    }

                    Text(
                        text = stringResource(id = R.string.search_results),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }

                items (
                    count = matchedPersonas.size,
                    key = { index -> matchedPersonas[index].uuid + "Matched" }
                ) { index ->
                    var botDetailDialogConfig: BotDetailDialogConfig? = null

                    if (matchedPersonas[index].parentUuid != "") {
                        val bot = personaListViewModel.getBot(matchedPersonas[index].parentUuid)

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
                        persona = matchedPersonas[index],
                        onClick = {
                            viewModel.persona.selectPersona(matchedPersonas[index].uuid)
                        },
                        onClickDelete = {
                            personaListViewModel.deletePersona(matchedPersonas[index].uuid)
                        },
                        botDetailDialogConfig = botDetailDialogConfig
                    )

                    Divider()
                }

                if (searchQuery != "" && matchedPersonas.isEmpty()) {
                    item {
                        NoMatches()
                    }
                }

                item{
                    if (searchQuery == "") {
                        return@item
                    }

                    Text(
                        text = stringResource(id = R.string.all_personas),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(20.dp)
                    )
                }

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
