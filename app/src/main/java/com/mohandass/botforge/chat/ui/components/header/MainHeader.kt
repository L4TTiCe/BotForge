// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Constants
import com.slaviboy.composeunits.adw

@Composable
fun MainHeader(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel
) {
    val title by viewModel.topBar.title
    val overrideMenu by viewModel.topBar.overrideMenu
    val menu by viewModel.topBar.menu

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (1.adw > Constants.FOLDABLE_THRESHOLD.dp && !overrideMenu) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.logo_cd),
                contentScale = ContentScale.Inside,
                modifier = modifier
                    .size(if (Constants.ICONS_SIZE.dp < 0.20.adw) Constants.ICONS_SIZE.dp + 10.dp else 0.20.adw)
                    .padding(5.dp)
            )
        }

        if (overrideMenu) {
            Spacer(modifier = Modifier.width(0.01.adw))

            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = stringResource(title),
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(20.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            menu()

            Spacer(modifier = Modifier.width(0.02.adw))
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 0.02.adw),
            ) {
                AvatarsBar(
                    modifier = Modifier
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
                        },
                    viewModel = viewModel
                )
            }

            DefaultDropdownMenu(modifier, viewModel)
        }
    }
}
