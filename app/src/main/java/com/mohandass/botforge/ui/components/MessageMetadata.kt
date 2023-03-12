package com.mohandass.botforge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.model.Message
import com.slaviboy.composeunits.dw

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
//               Row {
//                   Icon(
//                       painter = painterResource(id = R.drawable.baseline_attach_money_24),
//                       contentDescription = null,
//                       modifier = modifier.size(18.dp),
//                       tint = MaterialTheme.colorScheme.error
//                   )
//
//                   Text(
//                       text = (resources().getFloat(R.dimen.gpt_3_5_turbo_cost_per_1k_tokens)
//                                       * message.metadata?.totalTokens?.div(1000)!!).toString(),
//                       modifier = modifier,
//                       style = MaterialTheme.typography.labelSmall,
//                       color = MaterialTheme.colorScheme.error
//                   )
//               }
            }

            Spacer(modifier = modifier.width(0.2.dw))
        }
    }
}