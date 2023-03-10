package com.mohandass.botforge.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.viewmodels.AppViewModel

@Composable
fun MessageList(modifier: Modifier = Modifier, viewModel: AppViewModel = hiltViewModel()) {
    val messagesList by viewModel.activeChat

    LazyColumn(modifier = modifier) {
        items(messagesList, key = {it.uuid}) { item ->
            MessageEntry(modifier=Modifier, message = item, viewModel = viewModel)

            Spacer(modifier = modifier.height(10.dp))
        }

        item {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    viewModel.autoAddMessage()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
