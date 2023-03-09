package com.mohandass.botforge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppState
import com.mohandass.botforge.ui.viewmodels.AppViewModel
import com.slaviboy.composeunits.dh

@Composable
fun TopBar(appState: AppState?, viewModel: AppViewModel) {
    Surface(
        tonalElevation = 4.dp,
    ) {
        Column {
            Header(
                appState = appState,
                viewModel = viewModel
            )

            AvatarsBar(
                viewModel = viewModel
            )

            Spacer(
                modifier = Modifier.height(0.01.dh)
            )
        }
    }
}