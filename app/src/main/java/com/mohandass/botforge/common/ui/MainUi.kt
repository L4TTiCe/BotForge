// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.chat.ui.PersonaUi
import com.mohandass.botforge.chat.ui.components.header.top.TopBar
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.settings.ui.ApiKeyUi
import com.mohandass.botforge.settings.ui.IconCreditsUi
import com.mohandass.botforge.settings.ui.ManageAccountUi
import com.mohandass.botforge.settings.ui.SettingsUi
import com.mohandass.botforge.settings.ui.ApiUsageUi
import com.mohandass.botforge.ui.settings.AppInformationUi
import com.mohandass.botforge.ui.settings.OpenSourceLibrariesUi

/**
 * Main UI for the app
 *
 * Has NavigationHost for the different screens, including the
 * Persona, Settings, ApiKey, ApiUsage, AppInformation, ManageAccount,
 * OpenSourceLibraries and IconCredits screens
 *
 * App Hierarchy: MainActivity -> MainUi -> PersonaUi -> ...
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainUi(
    appState: AppState,
) {
    val navController = rememberAnimatedNavController()
    appState.setNavControllerMain(navController)

    Scaffold(
        topBar = {
            TopBar(appState = appState)
        },
        content = {
            Surface(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
            ) {
                Column {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = AppRoutes.MainRoutes.PersonaRoutes.Chat.route
                    ) {
                        composable(
                            route = AppRoutes.MainRoutes.PersonaRoutes.Chat.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            PersonaUi()
                        }
                        composable(
                            route = AppRoutes.MainRoutes.Settings.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            SettingsUi()
                        }
                        composable(
                            route = AppRoutes.MainRoutes.ApiKeySettings.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            ApiKeyUi(settingsViewModel = hiltViewModel())
                        }
                        composable(
                            route = AppRoutes.MainRoutes.ApiUsageSettings.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            ApiUsageUi(settingsViewModel = hiltViewModel())
                        }
                        composable(
                            route = AppRoutes.MainRoutes.ManageAccountSettings.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            ManageAccountUi(
                                settingsViewModel = hiltViewModel()
                            )
                        }
                        composable(
                            route = AppRoutes.MainRoutes.OpenSourceLicenses.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            OpenSourceLibrariesUi()
                        }
                        composable(
                            route = AppRoutes.MainRoutes.IconCredits.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            IconCreditsUi()
                        }
                        composable(
                            route = AppRoutes.MainRoutes.AppInformation.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )

                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { Constants.ANIMATION_OFFSET },
                                    animationSpec = tween(
                                        durationMillis = Constants.ANIMATION_DURATION,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(
                                    animationSpec = tween(Constants.ANIMATION_DURATION)
                                )
                            }
                        ) {
                            AppInformationUi()
                        }
                    }
                }
            }
        }
    )
}
