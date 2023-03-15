package com.mohandass.botforge.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.ui.components.TopBar
import com.mohandass.botforge.ui.settings.SettingsUi
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUi(appState: AppState?, viewModel: AppViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    viewModel.setNavController(navController)

    Scaffold(
        topBar = {
            TopBar(
                appState = appState,
                viewModel = viewModel
            )
        },
        content = {
            Surface(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
            ) {
                Column{
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.MainRoutes.PersonaRoutes.Chat.route
                    ) {
                        composable(AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                            PersonaUi(viewModel = viewModel)
                        }
                        composable(AppRoutes.MainRoutes.Settings.route) {
                            SettingsUi(viewModel = viewModel, settingsViewModel = hiltViewModel(), appState = appState)
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainUiPreview() {
    BotForgeTheme {
        MainUi(null)
    }
}
