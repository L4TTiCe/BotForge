package com.mohandass.botforge.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.ui.components.TopBar
import com.mohandass.botforge.ui.persona.PersonaUi
import com.mohandass.botforge.ui.settings.*
import com.mohandass.botforge.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUi(viewModel: AppViewModel) {
    val navController = rememberNavController()
    viewModel.setNavControllerMain(navController)

    Scaffold(
        topBar = {
            TopBar(
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
                            SettingsUi(viewModel = viewModel, settingsViewModel = hiltViewModel())
                        }
                        composable(AppRoutes.MainRoutes.ApiKeySettings.route) {
                            ApiKeyUi(settingsViewModel = hiltViewModel())
                        }
                        composable(AppRoutes.MainRoutes.ApiUsageSettings.route) {
                            ApiUsageUi(settingsViewModel = hiltViewModel())
                        }
                        composable(AppRoutes.MainRoutes.ManageAccountSettings.route) {
                            ManageAccountUi(viewModel = viewModel, settingsViewModel = hiltViewModel())
                        }
                        composable(AppRoutes.MainRoutes.OpenSourceLicenses.route) {
                            OpenSourceLibrariesUi(viewModel = viewModel)
                        }
                        composable(AppRoutes.MainRoutes.AppInformation.route) {
                            AppInformationUi()
                        }
                    }
                }
            }
        }
    )
}
