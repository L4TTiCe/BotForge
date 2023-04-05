// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.auth.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.Constants
import com.mohandass.botforge.auth.ui.components.GoogleSignInButton
import com.mohandass.botforge.auth.ui.components.SkipSignInButton
import com.mohandass.botforge.auth.viewmodel.LandingViewModel
import com.slaviboy.composeunits.adh

private const val TAG = "LandingUi"

@Composable
fun LandingUi(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    landingViewModel: LandingViewModel = hiltViewModel()
) {
    // On Start, check if the user is authenticated, if yes, then check if the OnBoarding is
    // completed, if yes, then navigate to the MainUi, otherwise navigate to the OnBoardingUi.
    LaunchedEffect(Unit) {
        landingViewModel.checkAuthentication {
            if (!landingViewModel.isOnBoardingCompleted()) {
                viewModel.logger.logVerbose(
                    TAG,
                    "checkAuthentication: Authenticated, navigating to OnBoardingUi"
                )
                viewModel.navController.navigate(AppRoutes.OnBoarding.route) {
                    popUpTo(AppRoutes.Landing.route) { inclusive = true }
                }
            } else {
                viewModel.logger.logVerbose(
                    TAG,
                    "checkAuthentication: Authenticated, navigating to MainUi"
                )
                viewModel.navController.navigate(AppRoutes.Main.route) {
                    popUpTo(AppRoutes.Landing.route) { inclusive = true }
                }
            }
        }
    }

    // set the system bars color to transparent
    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
        )

        onDispose {}
    }

    // launch google sign in activity and get the result
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                landingViewModel.onGoogleSignIn(credentials)

                viewModel.navController.navigate(AppRoutes.Landing.route) {
                    popUpTo(AppRoutes.Landing.route) { inclusive = true }
                }
            } catch (it: ApiException) {
                viewModel.logger.logError(TAG, "Google sign in failed", it)
            }
        }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.size(0.2.adh))

        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.size(0.05.adh))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.size(0.1.adh))

        Spacer(modifier = Modifier.size(0.01.adh))

        GoogleSignInButton {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.WEB_CLIENT_ID)
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            launcher.launch(googleSignInClient.signInIntent)
        }

        SkipSignInButton {
            landingViewModel.onSkip {
                viewModel.navController.navigate(AppRoutes.Landing.route) {
                    popUpTo(AppRoutes.Landing.route) { inclusive = true }
                }
            }
        }
    }
}
