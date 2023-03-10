package com.mohandass.botforge.ui.components

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
import com.mohandass.botforge.ui.theme.BotForgeTheme

@Composable
fun RoundedIconFromStringAnimated(
    modifier: Modifier = Modifier,
    text : String,
    onClick: () -> Unit = { }
) {
    val radialGradientColors = listOf(
//        MaterialTheme.colorScheme.primary,
//        MaterialTheme.colorScheme.onSecondaryContainer,
//        MaterialTheme.colorScheme.onPrimaryContainer,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.onTertiaryContainer,
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

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringAnimatedPreview() {
    BotForgeTheme {
        RoundedIconFromString(text = "Ab", modifier = Modifier.size(70.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringAnimatedPreviewEmoji() {
    BotForgeTheme {
        RoundedIconFromString(text = "\uD83D\uDD25", modifier = Modifier.size(70.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringAnimatedPreviewEmoji2() {
    BotForgeTheme {
        RoundedIconFromString(text = "❤️", modifier = Modifier.size(70.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconFromStringAnimatedPreviewEmoji3() {
    BotForgeTheme {
        RoundedIconFromString(text = "\uD83D\uDC80", modifier = Modifier.size(70.dp))
    }
}