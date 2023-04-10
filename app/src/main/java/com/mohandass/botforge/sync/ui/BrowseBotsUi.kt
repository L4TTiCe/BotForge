// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.common.ui.components.NoMatches
import com.mohandass.botforge.common.ui.components.SearchBar
import com.mohandass.botforge.sync.ui.components.BotCard
import com.slaviboy.composeunits.adh

@Composable
fun BrowseBotsUi(viewModel: AppViewModel) {
    val searchText by viewModel.browse.searchQuery
    val communityBots = viewModel.browse.fetchedBots
    val topBots = viewModel.browse.topBots

    val scrollState = rememberScrollState()

    var isUserGeneratedContentEnabled by remember {
        mutableStateOf(false)
    }

    val userPreferences by viewModel.userPreferences.observeAsState()
    userPreferences?.let {
        isUserGeneratedContentEnabled = it.enableUserGeneratedContent

        if (!isUserGeneratedContentEnabled) {
            viewModel.persona.clearSelection()
            viewModel.persona.setChatType(ChatType.CREATE)
            viewModel.persona.updateExpandCustomizePersona(true)
            viewModel.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                popUpTo(AppRoutes.MainRoutes.PersonaRoutes.Marketplace.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.persona.setChatType(ChatType.BROWSE)
        viewModel.browse.fetchBots()
        viewModel.browse.syncWithDatabase()
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
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.community),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )
                Text(
                    text = stringResource(id = R.string.community),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    viewModel.browse.syncWithDatabase()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cloud_sync_24),
                        contentDescription = stringResource(id = R.string.sync_cd),
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }

            SearchBar(
                searchQuery = searchText,
                onClear = {
                    viewModel.browse.updateSearchQuery("")
                },
                label = stringResource(id = R.string.community_search)
            ) {
                viewModel.browse.updateSearchQuery(it)
            }

            Text(
                text = stringResource(id = R.string.community_browse),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = stringResource(id = R.string.community_browse_message),
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (communityBots.isNotEmpty()) {
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 0.4.adh),
                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp)
                ) {
                    // use Bot's UUID as key
                    items(
                        count = communityBots.size,
                        key = { communityBots[it].uuid }
                    ) { idx ->
                        BotCard(
                            botE = communityBots[idx],
                            onClickButton = {
                                viewModel.browse.makePersona(communityBots[idx])
                            },
                            onUpVote = {
                                viewModel.browse.upVote(communityBots[idx].uuid)
                            },
                            onDownVote = {
                                viewModel.browse.downVote(communityBots[idx].uuid)
                            },
                            onReport = {
                                viewModel.browse.report(communityBots[idx].uuid)
                            }
                        )
                    }
                }
            } else if (searchText.isNotEmpty()) {
                NoMatches()
            }

            Text(
                text = stringResource(id = R.string.community_most_popular),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyHorizontalGrid(
                rows = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 0.4.adh),
                contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp)
            ) {
                // use Bot's UUID as key
                items(
                    count = topBots.size,
                    key = { topBots[it].uuid }
                ) { idx ->
                    BotCard(
                        botE = topBots[idx],
                        onClickButton = {
                            viewModel.browse.makePersona(topBots[idx])
                        },
                        onUpVote = {
                            viewModel.browse.upVote(topBots[idx].uuid)
                        },
                        onDownVote = {
                            viewModel.browse.downVote(topBots[idx].uuid)
                        },
                        onReport = {
                            viewModel.browse.report(topBots[idx].uuid)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(0.1.adh))
        }
    }
}
