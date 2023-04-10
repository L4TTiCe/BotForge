// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.common.Constants

@Composable
fun AvatarBarIcon(
    modifier: Modifier = Modifier,
    icon: Painter,
    iconContentDescription: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .size(Constants.ICONS_SIZE.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = icon,
            contentDescription = iconContentDescription,
            modifier = modifier
                .size(Constants.ICONS_SIZE.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .padding(10.dp)
                .clip(CircleShape)
                .scale(0.8f)
                .alpha(0.8f),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}