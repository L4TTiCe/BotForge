package com.mohandass.botforge.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.theme.BotForgeTheme

@Composable
fun AddAvatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    // Use a Box as the parent composable
    Box(modifier = modifier) {
        // Add a child composable for the circular image
        val color = MaterialTheme.colorScheme.onPrimaryContainer

        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .clip(CircleShape)
                .clickable { onClick() }
                // Align it to the center of the Box
                .align(Alignment.Center)
                .scale(0.5f),
            tint = MaterialTheme.colorScheme.inversePrimary
        )
        // Add another child composable for the circular border
        Canvas(
            modifier = Modifier
                // Match the size of the Box
                .matchParentSize()
                .align(Alignment.Center)
        ) {
            // Draw a circle with a stroke using MaterialTheme.colorScheme.onPrimaryContainer as color
            drawCircle(
                color = color,
                style = Stroke(width = 2.dp.toPx()),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAvatarIconPreview() {
    BotForgeTheme {
        AddAvatar(modifier = Modifier.size(90.dp))
    }
}
