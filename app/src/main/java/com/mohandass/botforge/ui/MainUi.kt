package com.mohandass.botforge.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.resources
import com.mohandass.botforge.ui.components.PersonaUi
import com.mohandass.botforge.ui.components.TopBar
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.ui.viewmodels.AppViewModel
import com.mohandass.botforge.R.string as AppText

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
                modifier = Modifier.padding(it),
            ) {
                Column{
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.MainRoutes.Default.route
                    ) {
                        composable(AppRoutes.MainRoutes.Default.route) {
                            PersonaUi(viewModel = viewModel)
                        }
                        composable(AppRoutes.MainRoutes.Settings.route) {
                            SettingsUi(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SettingsUi(viewModel: AppViewModel) {
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .fillMaxSize(),
    ) {
        Row {
            Text(
                text = resources().getString(AppText.settings),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {
                viewModel.navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier.size(32.dp),
                    contentDescription = "Back"
                )
            }
        }

        Text(
            text = "Version " + Utils.getAppVersion(),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Build " + Utils.getAppVersionCode(),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainUiPreview() {
    BotForgeTheme {
        MainUi(null)
    }
}
