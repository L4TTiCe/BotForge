// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header.top

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppState
import com.mohandass.botforge.chat.ui.components.header.MinimalHeader
import com.mohandass.botforge.settings.model.PreferredHeader
import com.slaviboy.composeunits.adh

val modifierWithFadeEdges = Modifier
    .fillMaxWidth()

    // Fade out the gradient at the start and end of the bar
    // Reference:
    // https://stackoverflow.com/questions/66762472/how-to-add-fading-edge-effect-to-android-jetpack-compose-column-or-row

    // Workaround to enable alpha compositing
    .graphicsLayer { alpha = 0.99F }
    .drawWithContent {
        val maxElements = 8

        // Create a list with a transparent color at the start and end,
        // with 5 black colors in between
        val colors = List(maxElements) { index ->
            if (index == 0 || index == maxElements - 1) {
                Color.Transparent
            } else {
                Color.Black
            }
        }

        drawContent()
        drawRect(
            brush = Brush.horizontalGradient(colors),
            blendMode = BlendMode.DstIn
        )
    }

@Composable
fun TopBar(appState: AppState) {

    var preferredHeader = remember {
        PreferredHeader.DEFAULT_HEADER
    }

    val userPreferences = appState.userPreferences.observeAsState()
    userPreferences.value?.let {
        preferredHeader = it.preferredHeader
    }

    Surface(
        tonalElevation = 4.dp,
    ) {
        Column {
            if (preferredHeader == PreferredHeader.MINIMAL_HEADER) {
                Spacer(
                    modifier = Modifier
                        .height(0.01.adh)
                        .fillMaxWidth()
                )
                MinimalHeader()
            } else {
                DefaultHeader()
            }
        }
    }
}
