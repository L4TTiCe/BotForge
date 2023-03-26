package com.mohandass.botforge.sync.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.common.ui.theme.BotForgeLightThemePreview
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.model.dao.entities.BotEProvider
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BotDetailDialog(bot: BotE, onClickDismiss: () -> Unit, onClickAccept: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClickDismiss,
        title = {
            Text(text = bot.name)
        },
        text = {
           Column {
               Text(
                   text = stringResource(id = R.string.description),
                   style = MaterialTheme.typography.labelMedium,
               )
               Text(
                   text = bot.description,
                   style = MaterialTheme.typography.bodyMedium
               )

               Spacer(modifier = Modifier.height(10.dp))

               if (bot.tags.isNotEmpty()) {
                   Text(
                       text = stringResource(id = R.string.tags),
                       style = MaterialTheme.typography.labelMedium,
                   )

                   LazyRow {
                       items(bot.tags.size) { tag ->
                           Chip(
                               onClick = { },
                               modifier = Modifier.padding(horizontal = 2.dp),
                               colors = ChipDefaults.chipColors(
                                      backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                                      contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                               )
                           ) {
                                  Text(
                                      text = bot.tags[tag],
                                        style = MaterialTheme.typography.bodySmall
                                  )
                           }
                       }
                   }
               }

               Spacer(modifier = Modifier.height(10.dp))

               Text(
                   text = stringResource(id = R.string.system_message),
                   style = MaterialTheme.typography.labelMedium,
               )

               Text(
                   text = bot.systemMessage,
                   style = MaterialTheme.typography.bodyMedium
               )
           }
        },
        confirmButton = {
            TextButton(onClick =  onClickAccept) {
                Text(
                    text = "Add",
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onClickDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                )
            }
        },
    )
}

@Preview
@Composable
fun BotDetailDialogPreview(
    @PreviewParameter(BotEProvider::class) bot: BotE,
) {
    BotForgeLightThemePreview {
        BotDetailDialog(bot, {}, {})
    }
}