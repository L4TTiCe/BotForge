package com.mohandass.botforge.ui.components.buttons

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.theme.BotForgeTheme

@Composable
fun TintedIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.plus,
    scale: Float = 0.5f,
    outerCircleColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconTint: Color = MaterialTheme.colorScheme.inversePrimary,
    onClick: () -> Unit = { }
) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .clip(CircleShape)
                .clickable { onClick() }
                .align(Alignment.Center)
                .scale(scale),
            tint = iconTint
        )

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center)
        ) {
            drawCircle(
                color = outerCircleColor,
                style = Stroke(width = 2.dp.toPx()),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TintedIconButtonPreview() {
    BotForgeTheme {
        TintedIconButton(modifier = Modifier.size(90.dp))
    }
}
