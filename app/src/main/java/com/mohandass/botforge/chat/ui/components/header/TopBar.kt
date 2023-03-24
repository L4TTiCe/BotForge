package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.chat.ui.components.header.MainHeader

@Composable
fun TopBar(viewModel: AppViewModel) {
    Surface(
        tonalElevation = 4.dp,
    ) {
        Column {
            MainHeader(
                viewModel = viewModel
            )
        }
    }
}