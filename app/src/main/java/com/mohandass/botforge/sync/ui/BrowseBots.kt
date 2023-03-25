package com.mohandass.botforge.sync.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseBots(viewModel: AppViewModel) {
    val searchText by viewModel.browse.searchQuery

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

                IconButton(onClick = { /*TODO*/ }) {
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
                    .padding(horizontal = 10.dp),
                value = searchText,
                onValueChange = { viewModel.browse.updateSearchQuery(it) },
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
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Browse bots from the community",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "DEBUG",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Row {
                Button(onClick = { viewModel.browse.addBot() }) {
                    Text("Add Bot")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = { viewModel.browse.getBots() }) {
                    Text("Log")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = { viewModel.browse.search() }) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = { viewModel.browse.deleteAllBots() }) {
                    Text("Delete All")
                }
            }


        }
    }
}