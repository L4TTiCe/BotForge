// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.common.ui.components.NoMatches
import com.mohandass.botforge.common.ui.components.SearchBar
import com.mohandass.botforge.sync.ui.components.BotCard
import com.mohandass.botforge.sync.viewmodel.BrowseViewModel
import com.slaviboy.composeunits.adh

@Composable
fun BrowseBotsUi(
    appViewModel: AppViewModel = hiltViewModel(),
    browseViewModel: BrowseViewModel = hiltViewModel(),
    personaViewModel: PersonaViewModel = hiltViewModel(),
) {
    val searchText by browseViewModel.searchQuery
    val communityBots = browseViewModel.fetchedBots

    val topBots = browseViewModel.topBots
    val recentBots = browseViewModel.recentBots
    val randomBots = browseViewModel.randomBots

    var isUserGeneratedContentEnabled by remember {
        mutableStateOf(false)
    }

    val userPreferences by appViewModel.appState.userPreferences.observeAsState()
    userPreferences?.let {
        isUserGeneratedContentEnabled = it.enableUserGeneratedContent

        if (!isUserGeneratedContentEnabled) {
            personaViewModel.clearSelection()
            personaViewModel.setChatType(ChatType.CREATE)
            personaViewModel.updateExpandCustomizePersona(true)
            appViewModel.appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                popUpTo(AppRoutes.MainRoutes.PersonaRoutes.Marketplace.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        personaViewModel.setChatType(ChatType.BROWSE)
        browseViewModel.fetchBots()
        browseViewModel.syncWithDatabase()
    }

    BackHandler {
        appViewModel.appState.navControllerPersona.popBackStack()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        tonalElevation = 0.1.dp,
    ) {
        LazyColumn {
            item {
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
                        browseViewModel.syncWithDatabase()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_cloud_sync_24),
                            contentDescription = stringResource(id = R.string.sync_cd),
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            }

            item {
                SearchBar(
                    searchQuery = searchText,
                    onClear = {
                        browseViewModel.updateSearchQuery("")
                    },
                    label = stringResource(id = R.string.community_search)
                ) {
                    browseViewModel.updateSearchQuery(it)
                }
            }

            item {
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
            }

            item {
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
                                    browseViewModel.makePersona(communityBots[idx])
                                },
                                onUpVote = {
                                    browseViewModel.upVote(communityBots[idx].uuid)
                                },
                                onDownVote = {
                                    browseViewModel.downVote(communityBots[idx].uuid)
                                },
                                onReport = {
                                    browseViewModel.report(communityBots[idx].uuid)
                                }
                            )
                        }
                    }
                } else if (searchText.isNotEmpty()) {
                    NoMatches()
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.community_most_popular),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
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
                                browseViewModel.makePersona(topBots[idx])
                            },
                            onUpVote = {
                                browseViewModel.upVote(topBots[idx].uuid)
                            },
                            onDownVote = {
                                browseViewModel.downVote(topBots[idx].uuid)
                            },
                            onReport = {
                                browseViewModel.report(topBots[idx].uuid)
                            }
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.community_explore),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 0.4.adh),
                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp)
                ) {
                    items(
                        count = randomBots.size,
                        key = { randomBots[it].uuid }
                    ) { idx ->
                        BotCard(
                            botE = randomBots[idx],
                            onClickButton = {
                                browseViewModel.makePersona(randomBots[idx])
                            },
                            onUpVote = {
                                browseViewModel.upVote(randomBots[idx].uuid)
                            },
                            onDownVote = {
                                browseViewModel.downVote(randomBots[idx].uuid)
                            },
                            onReport = {
                                browseViewModel.report(randomBots[idx].uuid)
                            }
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.community_most_recent),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 0.4.adh),
                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp)
                ) {
                    items(
                        count = recentBots.size,
                        key = { recentBots[it].uuid }
                    ) { idx ->
                        BotCard(
                            botE = recentBots[idx],
                            onClickButton = {
                                browseViewModel.makePersona(recentBots[idx])
                            },
                            onUpVote = {
                                browseViewModel.upVote(recentBots[idx].uuid)
                            },
                            onDownVote = {
                                browseViewModel.downVote(recentBots[idx].uuid)
                            },
                            onReport = {
                                browseViewModel.report(recentBots[idx].uuid)
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(0.1.adh))
            }
        }
    }
}
