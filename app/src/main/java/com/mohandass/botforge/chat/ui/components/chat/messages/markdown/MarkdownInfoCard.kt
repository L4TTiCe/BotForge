package com.mohandass.botforge.chat.ui.components.chat.messages.markdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.Role

// Card to inform users, that Message has been rendered as Markdown
@Composable
fun MarkdownInfoCard(
    modifier: Modifier = Modifier,
    isShownAsMarkdown: Boolean,
    cardColors: CardColors,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            },
        colors = cardColors,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isShownAsMarkdown) {
                Text(text = stringResource(id = R.string.markdown_default))
            } else {
                Text(text = stringResource(id = R.string.markdown_switch))
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                contentDescription = stringResource(id = R.string.show_as_md_cd)
            )
        }
    }
}

@Preview
@Composable
fun MarkdownInfoCardPreview() {
    var isShownAsMarkdown by remember { mutableStateOf(true) }

    MarkdownInfoCard(
        isShownAsMarkdown = isShownAsMarkdown,
        cardColors = Role.BOT.cardColors(),
        onClick = {
            isShownAsMarkdown = !isShownAsMarkdown
        }
    )
}
