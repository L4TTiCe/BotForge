package com.mohandass.botforge.chat.ui.components.chat.messages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TypingIndicatorDot(
    initialDelayMillis: Int,
) {
    val alpha = remember { Animatable(0.3f) }
    val offsetY = remember { Animatable(-2f) }

    LaunchedEffect(initialDelayMillis) {
        delay(initialDelayMillis.toLong())
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = DotAnimationDurationMillis,
                    delayMillis = DotAnimationDurationMillis,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
        )
    }

    LaunchedEffect(initialDelayMillis) {
        delay(initialDelayMillis.toLong())
        offsetY.animateTo(
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = DotAnimationBounceDurationMillis,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha.value)

    Box(
        Modifier
            .offset(y = offsetY.value.dp)
    ) {
        Box(
            Modifier
                .background(color, CircleShape)
                .size(6.dp)
        )
    }
}

const val initialDelayMillis: Int = 400
const val DotAnimationDurationMillis: Int = 400
const val DotAnimationBounceDurationMillis: Int = 800
