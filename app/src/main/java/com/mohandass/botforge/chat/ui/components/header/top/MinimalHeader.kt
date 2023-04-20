// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.header.top.AvatarsBar
import com.mohandass.botforge.chat.ui.components.header.top.DefaultDropdownMenu
import com.mohandass.botforge.chat.ui.components.header.top.modifierWithFadeEdges
import com.mohandass.botforge.common.Constants
import com.slaviboy.composeunits.adw

@Composable
fun MinimalHeader(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel(),
) {
    val title by appViewModel.appState.topBar.title
    val overrideMenu by appViewModel.appState.topBar.overrideMenu
    val menu by appViewModel.appState.topBar.menu

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
                    modifier = modifierWithFadeEdges,
                )
            }

            DefaultDropdownMenu()
        }
    }
}
