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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.ImageWithMessage
import com.mohandass.botforge.chat.ui.components.PersonaInfo
import com.mohandass.botforge.chat.ui.components.dialogs.DeleteAllPersonasDialog
import com.mohandass.botforge.chat.ui.components.header.HeaderWithActionIcon
import com.mohandass.botforge.chat.viewmodel.PersonaListViewModel
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.common.ui.components.NoMatches
import com.mohandass.botforge.common.ui.components.SearchBar
import com.mohandass.botforge.sync.ui.components.BotDetailDialogConfig
import com.mohandass.botforge.sync.viewmodel.BrowseViewModel
import com.slaviboy.composeunits.adh

@Composable
fun PersonaListUi(
    personaListViewModel: PersonaListViewModel = hiltViewModel(),
    personaViewModel: PersonaViewModel = hiltViewModel(),
    browseViewModel: BrowseViewModel = hiltViewModel(),
) {
    val personas by personaViewModel.personas.observeAsState(initial = emptyList())
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
        personaViewModel.setChatType(ChatType.LIST)
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
                                    browseViewModel.upVote(bot.uuid)
                                },
                                onDownVote = { browseViewModel.downVote(bot.uuid) },
                                onReport = { browseViewModel.report(bot.uuid) }
                            )
                        }
                    }

                    PersonaInfo(
                        persona = matchedPersonas[index],
                        onClick = {
                            personaViewModel.selectPersona(matchedPersonas[index].uuid)
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
                                    browseViewModel.upVote(bot.uuid)
                                },
                                onDownVote = { browseViewModel.downVote(bot.uuid) },
                                onReport = { browseViewModel.report(bot.uuid) }
                            )
                        }
                    }

                    PersonaInfo(
                        persona = personas[index],
                        onClick = {
                            personaViewModel.selectPersona(personas[index].uuid)
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
