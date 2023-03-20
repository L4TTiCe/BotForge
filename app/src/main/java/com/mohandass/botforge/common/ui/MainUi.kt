package com.mohandass.botforge.common.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.chat.ui.PersonaUi
import com.mohandass.botforge.chat.ui.components.TopBar
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.ui.settings.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainUi(viewModel: AppViewModel) {
    val navController = rememberAnimatedNavController()
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
                            PersonaUi(viewModel = viewModel)
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
                            SettingsUi(viewModel = viewModel, settingsViewModel = hiltViewModel())
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
                            ManageAccountUi(viewModel = viewModel, settingsViewModel = hiltViewModel())
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
                            OpenSourceLibrariesUi(viewModel = viewModel)
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
