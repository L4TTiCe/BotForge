package com.mohandass.botforge.chat.ui.components.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun RoundedIconFromString(
    modifier: Modifier = Modifier,
    text : String,
    borderColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                .drawBehind {
                    drawCircle(
                        color = borderColor,
                        style = Stroke(width = 5.dp.toPx())
                    )
                }
                .padding(top = 10.dp),
            style = MaterialTheme.typography.displaySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 1,
        )
    }
}
