package com.mohandass.botforge.sync.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.sync.ui.components.BotCard
import com.slaviboy.composeunits.dh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseBots(viewModel: AppViewModel) {
    val searchText by viewModel.browse.searchQuery
    val communityBots = viewModel.browse.fetchedBots
    val topBots = viewModel.browse.topBots

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.browse.fetchBots()
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
                    text = "Community",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    viewModel.browse.syncWithDatabase()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cloud_sync_24),
                        contentDescription = "Sync",
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = searchText,
                onValueChange = {
                    viewModel.browse.updateSearchQuery(it)
                    viewModel.browse.search()
                },
                label = {
                    Text("Search Community Bots")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { viewModel.browse.updateSearchQuery("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_clear_24),
                            contentDescription = "Search",
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search, keyboardType = KeyboardType.Text)
            )

            Text(
                text = "Browse Bots",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Browse bots from the community",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyHorizontalGrid(
                rows = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 0.4.dh),
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
                            viewModel.browse.makePersona(topBots[idx])
                        }
                    )
                }
            }

            Text(
                text = "Most Popular Bots",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyHorizontalGrid(
                rows = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 0.4.dh),
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
                        }
                    )
                }
            }
        }
    }
}