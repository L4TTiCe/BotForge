// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.common.Constants

@Composable
fun ActiveIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(18.dp) // Matches the height of other elements in the row
            .width(Constants.ICONS_SIZE.dp),
        contentAlignment = Alignment.Center // Centers its content vertically and horizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(2.dp) // Specifies the height of the spacer inside the box
                .width(24.dp)
                .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
        )
    }
}
