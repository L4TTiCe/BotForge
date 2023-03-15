package com.mohandass.botforge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.theme.BotForgeTheme

@Composable
fun RoundedIcon(
    image: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
//            .border(
//                width = 2.dp,
//                color = MaterialTheme.colorScheme.onPrimaryContainer,
//                shape = CircleShape
//            )
            .padding(6.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun RoundedIconPreview() {
    BotForgeTheme {
        RoundedIcon(image = painterResource(id = R.drawable.logo), modifier = Modifier.size(70.dp))
    }
}