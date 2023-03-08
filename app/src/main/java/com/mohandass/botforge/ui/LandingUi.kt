package com.mohandass.botforge.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppState
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.ui.components.SkipSignInButton
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.ui.viewmodels.LandingViewModel
import com.slaviboy.composeunits.dh

@Composable
fun LandingUi(modifier: Modifier = Modifier, appState: AppState?, viewModel: LandingViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.checkAuthentication {
            Log.v("LandingUi", "checkAuthentication: Authenticated, navigating to MainUi")
            appState?.navController?.navigate(AppRoutes.Main.route)
        }
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
            text = "BotForge",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.size(0.05.dh))

        Image(
            painter = painterResource(id = com.mohandass.botforge.R.drawable.logo),
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
                text = "Sign In / Sign Up",
                modifier = Modifier.padding(8.dp)
            )
        }

        SkipSignInButton {
            viewModel.onSkip{
                appState?.navController?.navigate(AppRoutes.Main.route)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LandingUiPreview() {
    BotForgeTheme {
        LandingUi(appState = null)
    }
}
