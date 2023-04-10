// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.icons

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedIconFromStringAnimated(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = { }
) {
    val radialGradientColors = listOf(
//        MaterialTheme.colorScheme.primary,
//        MaterialTheme.colorScheme.onSecondaryContainer,
//        MaterialTheme.colorScheme.onPrimaryContainer,

//        MaterialTheme.colorScheme.tertiary,
//        MaterialTheme.colorScheme.onTertiaryContainer,

        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.colorScheme.tertiary,
    )

    val infiniteTransition = rememberInfiniteTransition()
    val rotateAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            )
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            modifier = modifier
                .clip(CircleShape)
                .drawBehind {
                    rotate(rotateAnimation.value) {
                        drawCircle(
                            Brush.verticalGradient(
                                colors = radialGradientColors,
                            ),
                            style = Stroke(width = 5.dp.toPx())
                        )
                    }
                }
                .padding(top = 10.dp),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
fun RoundedIconFromStringAnimatedPreview() {
    RoundedIconFromString(
        modifier = Modifier.size(90.dp),
        text = "AB"
    )
}

@Preview
@Composable
fun RoundedIconFromStringAnimatedPreviewEmoji() {
    RoundedIconFromString(
        modifier = Modifier.size(90.dp),
        text = "F"
    )
}
