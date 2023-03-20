package com.mohandass.botforge.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.ui.viewmodel.LandingViewModel
import com.slaviboy.composeunits.dh

@Composable
fun LandingUi(modifier: Modifier = Modifier, appState: AppState?, viewModel: LandingViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.checkAuthentication {
            Log.v("LandingUi", "checkAuthentication: Authenticated, navigating to MainUi")
            appState?.navController?.navigate(AppRoutes.Main.route)
        }
    }

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
        )

        onDispose {}
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.size(0.2.dh))

        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.size(0.05.dh))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.size(0.15.dh))

        FilledTonalButton (
            onClick = {
                appState?.navController?.navigate(AppRoutes.SignIn.route)
            }
        ) {
            Text(
                text = stringResource(id = R.string.sign_in_or_sign_up),
                modifier = Modifier.padding(8.dp)
            )
        }

        TextButton(
            onClick = {
                viewModel.onSkip{
                    appState?.navController?.navigate(AppRoutes.Main.route)
                }
            },
            modifier = Modifier.padding(8.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = stringResource(id = R.string.anonymous_sign_in),)
        }
    }
}
