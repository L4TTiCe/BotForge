// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.header.AvatarsBar
import com.mohandass.botforge.chat.ui.components.header.modifierWithFadeEdges
import com.mohandass.botforge.common.Constants
import com.slaviboy.composeunits.adw

@Composable
fun DefaultHeader(
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
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo_cd),
            contentScale = ContentScale.Inside,
            modifier = modifier
                .size(if (Constants.ICONS_SIZE.dp < 0.20.adw) Constants.ICONS_SIZE.dp else 0.20.adw)
                .padding(10.dp)
        )

        // If there is more space than the threshold, show the avatars bar on Header
        // Allows more space for the Messages Below
        // Triggered when in landscape mode, Fold-able of Tablet
        if (1.adw > Constants.FOLDABLE_THRESHOLD.dp
            // If overrideMenu is true, don't show the avatars bar
            // As the App is in a different screen (Settings, About, etc.) than Chat
            && !overrideMenu) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 0.02.adw),
            ) {
                AvatarsBar(
                    modifier = modifierWithFadeEdges,
                    viewModel = viewModel
                )
            }
        } else {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = stringResource(title),
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(10.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        if (overrideMenu) {

            menu()

            Spacer(modifier = Modifier.width(0.02.adw))
        } else {
            DefaultDropdownMenu(viewModel)
        }

    }
}
