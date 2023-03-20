package com.mohandass.botforge.chat.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.chat.ui.components.AvatarsBar
import com.mohandass.botforge.AppViewModel
import com.slaviboy.composeunits.dh

@Composable
fun PersonaUi(viewModel: AppViewModel) {
    val navController = rememberNavController()
    viewModel.setNavControllerPersona(navController)

    val isLoading by viewModel.isLoading

    LaunchedEffect(key1 = viewModel) {
        viewModel.persona.fetchPersonas()
    }

    var activeTheme = remember {
        PreferredTheme.AUTO
    }
    val userPreferences = viewModel.userPreferences.observeAsState()
    userPreferences.value?.let {
        activeTheme = it.preferredTheme
    }

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    val statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
    val navigationBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(0.1.dp)

    DisposableEffect(systemUiController, activeTheme) {

        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = when (activeTheme) {
                PreferredTheme.AUTO -> useDarkIcons
                PreferredTheme.LIGHT -> true
                PreferredTheme.DARK -> false
            }
        )

        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = when (activeTheme) {
                PreferredTheme.AUTO -> useDarkIcons
                PreferredTheme.LIGHT -> true
                PreferredTheme.DARK -> false
            }
        )

        onDispose {}
    }

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