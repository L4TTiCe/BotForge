package com.mohandass.botforge

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.ui.*
import com.mohandass.botforge.ui.auth.SignInUi
import com.mohandass.botforge.ui.auth.SignUpUi
import com.mohandass.botforge.common.ui.theme.BotForgeTheme
import com.slaviboy.composeunits.initSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        initSize()

        setContent {
            val viewModel: AppViewModel = hiltViewModel()
            BotForgeTheme(viewModel = viewModel) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val appState = rememberAppState(snackbarHostState)

                    viewModel.setNavController(appState.navController)

                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                    ) {
                        Navigation(modifier = Modifier.padding(it), appState = appState, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberAnimatedNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        AppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(modifier: Modifier, appState: AppState, viewModel: AppViewModel) {
    AnimatedNavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = AppRoutes.Splash.route) {
        composable("test") {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {

            }
        }

        composable(
            route = AppRoutes.Splash.route,
            exitTransition = {
                fadeOut(
                    animationSpec = tween(Constants.ANIMATION_DURATION)
                )
            }
        ) {
            SplashUi(appState = appState)
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
            LandingUi(appState = appState)
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
            MainUi(viewModel = viewModel)
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
            SignUpUi(appState = appState)
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
            SignInUi(appState = appState)
        }
    }
}