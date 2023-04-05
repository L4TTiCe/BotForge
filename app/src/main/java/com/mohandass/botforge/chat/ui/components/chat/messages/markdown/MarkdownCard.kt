// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.chat.messages.markdown

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.common.Samples
import dev.jeziellago.compose.markdowntext.MarkdownText

// Shows a Card with Markdown content
// Used for showing the Message content in the Chat as Markdown
@Composable
fun MarkdownCard(
    modifier: Modifier = Modifier,
    markdown: String,
    role: Role,
    isVisible: Boolean
) {
    if (!isVisible) return

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = role.cardColors(),
        shape = MaterialTheme.shapes.small,
    ) {
        MarkdownText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = role.markdownColor(),
            markdown = markdown
        )
    }
}

@Preview
@Composable
fun MarkdownCardPreview() {
    MarkdownCard(
        markdown = Samples.MARKDOWN,
        role = Role.BOT,
        isVisible = true
    )
}
