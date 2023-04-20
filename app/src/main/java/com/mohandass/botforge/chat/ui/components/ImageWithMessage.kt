// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun ImageWithMessage(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    painter: Painter,
    imageContentDescription: String? = null,
    message: String,
) {
    if (!visible) return

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painter,
                modifier = Modifier
                    .size(150.dp)
                    .alpha(0.8f),
                contentDescription = imageContentDescription
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.fillMaxWidth(0.7f),
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageWithMessagePreviewBookmarks() {
    ImageWithMessage(
        painter = painterResource(id = R.drawable.empty_box),
        imageContentDescription = stringResource(id = R.string.no_bookmarks_cd),
        message = stringResource(id = R.string.no_bookmarks),
    )
}

@Preview(showBackground = true)
@Composable
fun ImageWithMessagePreviewPersonas() {
    ImageWithMessage(
        painter = painterResource(id = R.drawable.empty_box),
        message = stringResource(id = R.string.no_personas_yet),
    )
}
