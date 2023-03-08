package com.mohandass.botforge.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.ui.theme.BotForgeTheme

@Composable
fun RoundedIconFromString(
    text : String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Column(
        modifier = modifier.fillMaxSize()
            .clip(CircleShape)
            .clickable { onClick() }.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            modifier = modifier
                .clip(CircleShape)
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    CircleShape
                )
                .padding(top = 10.dp),
            style = MaterialTheme.typography.displaySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringPreview() {
    BotForgeTheme {
        RoundedIconFromString(text = "Ab", modifier = Modifier.size(70.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringPreviewEmoji() {
    BotForgeTheme {
        RoundedIconFromString(text = "\uD83D\uDD25", modifier = Modifier.size(70.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringPreviewEmoji2() {
    BotForgeTheme {
        RoundedIconFromString(text = "❤️", modifier = Modifier.size(70.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringPreviewEmoji3() {
    BotForgeTheme {
        RoundedIconFromString(text = "\uD83D\uDC80", modifier = Modifier.size(70.dp))
    }
}