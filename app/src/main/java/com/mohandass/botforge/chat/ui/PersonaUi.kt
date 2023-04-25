// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.header.top.AvatarsBar
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.image.ui.ImageUi
import com.mohandass.botforge.settings.model.PreferredHeader
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.sync.ui.BrowseBotsUi
import com.mohandass.botforge.sync.ui.SharePersonaUi
import com.slaviboy.composeunits.adh
import com.slaviboy.composeunits.adw

/**
 * Main UI for the persona screen
 *
 * Has NavigationHost for the different screens, including the
 * Chat, History, Browse and Share screens
 *
 * App Hierarchy: MainActivity -> MainUi -> PersonaUi -> ...
 */
@Composable
fun PersonaUi(
    appViewModel: AppViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    appViewModel.appState.setNavControllerPersona(navController)

    val isLoadingChat by appViewModel.appState.isChatLoading
    val isLoadingImage by appViewModel.appState.isImageLoading

    LaunchedEffect(Unit) {
        appViewModel.appState.topBar.title.value = R.string.app_name
        appViewModel.appState.topBar.overrideMenu.value = false
    }

    var activeTheme = remember {
        PreferredTheme.AUTO
    }
    var preferredHeader = remember {
        PreferredHeader.DEFAULT_HEADER
    }

    val userPreferences = appViewModel.appState.userPreferences.observeAsState()
    userPreferences.value?.let {
        activeTheme = it.preferredTheme
        preferredHeader = it.preferredHeader
    }

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    val statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)

    // set the status bar and navigation bar colors to App theme colors
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
            color = Color.Transparent,
            darkIcons = when (activeTheme) {
                PreferredTheme.AUTO -> useDarkIcons
                PreferredTheme.LIGHT -> true
                PreferredTheme.DARK -> false
            }
        )

        onDispose {}
    }

    // Avatar bar and progress bar on top of the screen
    Column {
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {

                // Show avatars bar, If using Default Header,
                // and the screen size in small
                // Else, Avatar Bar will be in Header
                if (1.adw < Constants.FOLDABLE_THRESHOLD.dp &&
                    preferredHeader == PreferredHeader.DEFAULT_HEADER
                ) {
                    AvatarsBar()
                }

                Spacer(
                    modifier = Modifier.height(0.01.adh)
                )

                LinearProgressIndicator(
                    modifier = Modifier
                        .alpha(if (isLoadingChat || isLoadingImage) 0.9f else 0f)
                        .fillMaxWidth(),
                )
            }
        }

        // Navigation host for the different screens within
        NavHost(
            navController = navController,
            startDestination = AppRoutes.MainRoutes.PersonaRoutes.Chat.route
        ) {
            composable(AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                ChatUi()
            }
            composable(AppRoutes.MainRoutes.PersonaRoutes.Image.route) {
                ImageUi()
            }
            composable(AppRoutes.MainRoutes.PersonaRoutes.History.route) {
                HistoryUi()
            }
            composable(AppRoutes.MainRoutes.PersonaRoutes.Marketplace.route) {
                BrowseBotsUi()
            }
            composable(AppRoutes.MainRoutes.PersonaRoutes.Share.route) {
                SharePersonaUi()
            }
            composable(AppRoutes.MainRoutes.PersonaRoutes.List.route) {
                PersonaListUi()
            }
        }
    }
}
