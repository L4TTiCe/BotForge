package com.mohandass.botforge.ui.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.R
import com.mohandass.botforge.viewmodels.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MessageList(modifier: Modifier = Modifier, viewModel: AppViewModel = hiltViewModel()) {
    val messagesList by viewModel.activeChat
    val handleDelete by viewModel.handleDelete

    // Visibility,
    var visibility by remember { mutableStateOf(true) }

    fun handleClearAllMessages() {
        visibility = false

        viewModel.viewModelScope.launch {
            delay(500)
            viewModel.clearMessages()
            visibility = true
        }

        viewModel.handleDelete(false)
    }

    if (handleDelete) {
        handleClearAllMessages()
    }

    AnimatedVisibility(
        visible = visibility,
        exit = slideOutHorizontally(targetOffsetX = { it + 200 }),
    ) {
        Column(modifier = modifier) {
            messagesList.forEach { item ->
                MessageEntry(modifier=Modifier, message = item, viewModel = viewModel)
                Spacer(modifier = modifier.height(12.dp))
            }


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
