// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.mohandass.botforge.auth.ui.LandingUi
import com.mohandass.botforge.auth.ui.SignInUi
import com.mohandass.botforge.auth.ui.SignUpUi
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.services.snackbar.SnackbarLauncher
import com.mohandass.botforge.common.services.snackbar.SnackbarLauncherLocation
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.services.snackbar.SwipeableSnackbarHost
import com.mohandass.botforge.common.ui.MainUi
import com.mohandass.botforge.common.ui.SplashUi
import com.mohandass.botforge.common.ui.theme.BotForgeTheme
import com.mohandass.botforge.onboarding.ui.OnBoarding
import com.mohandass.botforge.ui.*
import com.slaviboy.composeunits.initSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * Main Activity.
 *
 * Holds the main Navigation Graph. The Navigation Graph is composed of the following destinations:
 * - Splash Screen
 * - Landing Screen
 * - Sign In Screen
 * - Sign Up Screen
 * - Main Screen
 * - OnBoarding Screen
 *
 * Also holds the SnackbarLauncher responsible for showing snackbars.
 *
 * App Hierarchy: MainActivity -> MainUi -> PersonaUi -> ...
 */
@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject lateinit var appState: AppState

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        initSize()

        // Initialize Firebase App Check
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        setContent {
            appState.resources = resources()

            BotForgeTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    rememberSnackbarLauncher(
                        snackbarHostState,
                        snackbarManager = SnackbarManager.getInstance(SnackbarLauncherLocation.MAIN)
                    )

                    appState.setNavController(rememberAnimatedNavController())

                    Scaffold(
                        snackbarHost = { SwipeableSnackbarHost(snackbarHostState) },
                    ) {
                        Navigation(
                            modifier = Modifier.padding(top = it.calculateTopPadding()),
                            appState = appState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun rememberSnackbarLauncher(
    snackbarHostState: SnackbarHostState,
    snackbarManager: SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, snackbarManager, resources, coroutineScope) {
    SnackbarLauncher(
        snackbarHostState = snackbarHostState,
        snackbarManager = snackbarManager,
        resources = resources,
        coroutineScope = coroutineScope
    )
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(modifier: Modifier, appState: AppState) {
    AnimatedNavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = AppRoutes.Splash.route
    ) {
        composable(
            route = AppRoutes.Splash.route,
            exitTransition = {
                fadeOut(
                    animationSpec = tween(Constants.ANIMATION_DURATION)
                )
            }
        ) {
            SplashUi()
        }
        composable(
            route = AppRoutes.Landing.route,
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
            LandingUi()
        }
        composable(
            route = AppRoutes.Main.route,
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
            MainUi(appState = appState)
        }
        composable(
            route = AppRoutes.SignUp.route,
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
            SignUpUi()
        }
        composable(
            route = AppRoutes.SignIn.route,
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
            SignInUi()
        }
        composable(
            route = AppRoutes.OnBoarding.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { Constants.ANIMATION_OFFSET },
                    animationSpec = tween(
                        durationMillis = Constants.ANIMATION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(
                    animationSpec = tween(Constants.ANIMATION_DURATION)
                )

            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { -Constants.ANIMATION_OFFSET },
                    animationSpec = tween(
                        durationMillis = Constants.ANIMATION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(
                    animationSpec = tween(Constants.ANIMATION_DURATION)
                )
            },
            popEnterTransition = {
                slideInVertically(
                    initialOffsetY = { -Constants.ANIMATION_OFFSET },
                    animationSpec = tween(
                        durationMillis = Constants.ANIMATION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(
                    animationSpec = tween(Constants.ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { Constants.ANIMATION_OFFSET },
                    animationSpec = tween(
                        durationMillis = Constants.ANIMATION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(
                    animationSpec = tween(Constants.ANIMATION_DURATION)
                )
            }
        ) {
            OnBoarding()
        }
    }
}
