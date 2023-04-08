// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.slaviboy.composeunits.adh

@Composable
fun TopBar(viewModel: AppViewModel) {
    Surface(
        tonalElevation = 4.dp,
    ) {
        Column {
            Spacer(
                modifier = Modifier
                    .height(0.01.adh)
                    .fillMaxWidth()
            )
            MainHeader(
                viewModel = viewModel
            )
        }
    }
}