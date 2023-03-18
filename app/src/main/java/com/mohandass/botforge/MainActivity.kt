package com.mohandass.botforge

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.ui.*
import com.mohandass.botforge.ui.auth.SignInUi
import com.mohandass.botforge.ui.auth.SignUpUi
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.viewmodels.AppViewModel
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

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
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

@Composable
fun Navigation(modifier: Modifier, appState: AppState, viewModel: AppViewModel) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = AppRoutes.Splash.route) {
        composable("test") {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {

            }
        }

        composable(AppRoutes.Splash.route) {
            SplashUi(appState = appState)
        }
        composable(AppRoutes.Landing.route) {
            LandingUi(appState = appState)
        }
        composable(AppRoutes.Main.route) {
            MainUi(viewModel = viewModel)
        }
        composable(AppRoutes.SignUp.route) {
            SignUpUi(appState = appState)
        }
        composable(AppRoutes.SignIn.route) {
            SignInUi(appState = appState)
        }
    }
}