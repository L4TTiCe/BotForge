package com.mohandass.botforge.chat.ui.components.chat.messages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Message

@Composable
fun MessageMetadata(modifier: Modifier = Modifier, message: Message) {
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

        Text(
            text = "ID: ${message.metadata?.openAiId}",
            modifier = modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "FinishReason: ${message.metadata?.finishReason}",
            modifier = modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row {
            Column {
                Text(
                    text = "PromptTokens: ${message.metadata?.promptTokens}",
                    modifier = modifier,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "ResponseTokens: ${message.metadata?.completionTokens}",
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

                    Text(
                        text = "${message.metadata?.totalTokens}",
                        modifier = modifier,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = modifier.width(10.dp))
        }

        Spacer(modifier = modifier.height(10.dp))
    }
}