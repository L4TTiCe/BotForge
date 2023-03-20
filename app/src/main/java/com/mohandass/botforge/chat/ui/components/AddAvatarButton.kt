package com.mohandass.botforge.chat.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun AddAvatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {

    Box(modifier = modifier) {
        val color = MaterialTheme.colorScheme.onPrimaryContainer

        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .clip(CircleShape)
                .clickable { onClick() }
                .align(Alignment.Center)
                .scale(0.5f),
            tint = MaterialTheme.colorScheme.inversePrimary
        )

        Canvas(
            modifier = Modifier
                // Match the size of the Box
                .matchParentSize()
                .align(Alignment.Center)
        ) {
            drawCircle(
                color = color,
                style = Stroke(width = 4.dp.toPx()),
            )
        }
    }

}

