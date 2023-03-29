package com.mohandass.botforge.chat.ui.components.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        TypingIndicatorDot(0 * initialDelayMillis)
        TypingIndicatorDot(1 * initialDelayMillis)
        TypingIndicatorDot(2 * initialDelayMillis)
    }
}

@Preview
@Composable
fun TypingIndicatorPreview() {
    TypingIndicator()
}