package com.mohandass.botforge.ui.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.R
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.Role
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.viewmodels.AppViewModel
import com.slaviboy.composeunits.dw
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageEntry(
    modifier: Modifier = Modifier,
    message: Message = Message(),
    viewModel: AppViewModel
) {
    val showMetadata = remember { mutableStateOf(false) }
    var messageContent by remember { mutableStateOf(message.text) }
    var isUser by remember { mutableStateOf(message.role.isUser()) }

    val roles by remember {
        mutableStateOf(listOf("User", "Bot"))
    }

    val userColor = MaterialTheme.colorScheme.primary
    val botColor = MaterialTheme.colorScheme.tertiary

    val colors by remember {
        mutableStateOf(listOf(userColor, botColor))
    }

    val botTextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        cursorColor = MaterialTheme.colorScheme.tertiary,
        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary
    )

    // Visibility, used to animate the message entry and exit
    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visibility = true
    }

    fun handleDelete() {
        visibility = false

        // launch coroutine to delete message after animation
        viewModel.viewModelScope.launch {
            delay(500)
            viewModel.deleteMessage(
            Message(messageContent,
                if (isUser) Role.USER else Role.BOT, message.uuid))
        }
    }

    AnimatedVisibility(
        visible = visibility,
        enter = slideInHorizontally(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetX = { it + 200 }
        ),
        exit = slideOutHorizontally(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            targetOffsetX = { it + 200 }
        ),
    ) {
        Column {
            Text(
                text = roles[if (isUser) 0 else 1],
                modifier = modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = colors[if (isUser) 0 else 1]
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = messageContent,
                    onValueChange = {
                        messageContent = it
                        viewModel.updateMessage(Message(messageContent,
                            if (isUser) Role.USER else Role.BOT, message.uuid))
                    },
                    modifier = modifier
                        .fillMaxWidth(0.85f)
                        .sizeIn(minHeight = 100.dp),
                    trailingIcon = {
                        IconButton(onClick = { isUser = !isUser }) {
                            Icon(
                                painter = painterResource(id = R.drawable.swap),
                                modifier = Modifier.size(18.dp),
                                contentDescription = null,
                            )
                        }
                    },
                    colors = if (isUser) TextFieldDefaults.textFieldColors() else botTextFieldColors,
                )

                Column {
                    // Delete button
                    IconButton(onClick = {
                        handleDelete()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }

                    if (message.metadata != null) {
                        // Metadata button
                        IconButton(onClick = {
                            showMetadata.value = !showMetadata.value
                        }) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        }
                    }
                }
            }

           AnimatedVisibility(visible = showMetadata.value) {
               Column {
                   Spacer(modifier = modifier.height(4.dp))

                   Text(
                       text = "Metadata",
                       modifier = modifier
                           .fillMaxWidth(),
                       style = MaterialTheme.typography.labelMedium,
                       color = MaterialTheme.colorScheme.onSurface
                   )

                   Spacer(modifier = modifier.height(4.dp))

                   Text(text = "ID: ${message.metadata?.openAiId}",
                       modifier = modifier
                           .fillMaxWidth(),
                       style = MaterialTheme.typography.labelSmall,
                       color = MaterialTheme.colorScheme.onSurface
                   )

                   Text(text = "FinishReason: ${message.metadata?.finishReason}",
                       modifier = modifier
                           .fillMaxWidth(),
                       style = MaterialTheme.typography.labelSmall,
                       color = MaterialTheme.colorScheme.onSurface
                   )

                   Row {
                       Column {
                           Text(text = "PromptTokens: ${message.metadata?.promptTokens}",
                               modifier = modifier,
                               style = MaterialTheme.typography.labelSmall,
                               color = MaterialTheme.colorScheme.onSurface
                           )

                           Text(text = "ResponseTokens: ${message.metadata?.completionTokens}",
                               modifier = modifier,
                               style = MaterialTheme.typography.labelSmall,
                               color = MaterialTheme.colorScheme.onSurface
                           )
                       }

                       Spacer(modifier = modifier.weight(1f))

                       Column {
                           Row {
                               Icon(
                                      painter = painterResource(id = R.drawable.baseline_token_24),
                                      contentDescription = null,
                                      modifier = modifier.size(18.dp)
                                 )

                               Spacer(modifier = modifier.width(4.dp))

                               Text(text = "${message.metadata?.totalTokens}",
                                   modifier = modifier,
                                   style = MaterialTheme.typography.labelSmall,
                                   color = MaterialTheme.colorScheme.onSurface
                               )
                           }
//                           Row {
//                               Icon(
//                                   painter = painterResource(id = R.drawable.baseline_attach_money_24),
//                                   contentDescription = null,
//                                   modifier = modifier.size(18.dp),
//                                   tint = MaterialTheme.colorScheme.error
//                               )
//
//                               Text(
//                                   text = (resources().getFloat(R.dimen.gpt_3_5_turbo_cost_per_1k_tokens)
//                                                   * message.metadata?.totalTokens?.div(1000)!!).toString(),
//                                   modifier = modifier,
//                                   style = MaterialTheme.typography.labelSmall,
//                                   color = MaterialTheme.colorScheme.error
//                               )
//                           }
                       }

                       Spacer(modifier = modifier.width(0.2.dw))
                   }
               }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageEntryPreview() {
    BotForgeTheme {
        MessageEntry(viewModel = hiltViewModel())
    }
}