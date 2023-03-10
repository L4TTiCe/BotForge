package com.mohandass.botforge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppState
import com.mohandass.botforge.ui.viewmodels.AppViewModel

@Composable
fun TopBar(appState: AppState?, viewModel: AppViewModel) {
    Surface(
        tonalElevation = 4.dp,
    ) {
        Column {
            MainHeader(
                appState = appState,
                viewModel = viewModel
            )
        }
    }
}