package com.mohandass.botforge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.ui.components.AvatarsBar
import com.mohandass.botforge.ui.persona.HistoryUi
import com.mohandass.botforge.viewmodels.AppViewModel
import com.slaviboy.composeunits.dh

@Composable
fun PersonaUi(viewModel: AppViewModel) {
    val navController = rememberNavController()
    viewModel.setNavControllerPersona(navController)

    val isLoading by viewModel.isLoading

    Column {
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                AvatarsBar(
                    viewModel = viewModel
                )

                Spacer(
                    modifier = Modifier.height(0.01.dh)
                )

                LinearProgressIndicator(
                    modifier = Modifier
                        .alpha(if (isLoading) 0.9f else 0f)
                        .fillMaxWidth(),
                )
            }
        }

        NavHost(
            navController = navController,
            startDestination = AppRoutes.MainRoutes.PersonaRoutes.Chat.route
        ) {
            composable(AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                ChatUi(viewModel = viewModel)
            }
            composable(AppRoutes.MainRoutes.PersonaRoutes.History.route) {
                HistoryUi(viewModel = viewModel)
            }
        }
    }
}