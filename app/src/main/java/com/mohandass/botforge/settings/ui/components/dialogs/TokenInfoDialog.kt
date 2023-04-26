// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.ui.components.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.resources

@Composable
fun TokenInfoDialog(
    onDismiss: () -> Unit,
    context: Context
) {
    AlertDialog(onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.managing_tokens))
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.managing_tokens_message))

                val annotatedStringManagingTokens = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append(resources().getString(R.string.learn_more_managing_tokens) + " ")
                    }
                    addStringAnnotation(
                        tag = "URL",
                        annotation = resources().getString(R.string.open_ai_managing_tokens_link),
                        start = length,
                        end = length + resources().getString(R.string.open_ai_managing_tokens_link).length
                    )
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(resources().getString(R.string.open_ai_managing_tokens))
                    }
                }

                ClickableText(
                    text = annotatedStringManagingTokens,
                    modifier = Modifier.padding(vertical = 10.dp),
                    onClick = { offset ->
                        annotatedStringManagingTokens.getStringAnnotations(
                            tag = "URL",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let { annotation ->
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(annotation.item)
                                }
                                context.startActivity(intent)
                            }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        },
        dismissButton = {}
    )
}
