package com.mohandass.botforge.auth.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.mohandass.botforge.auth.viewmodel.LandingViewModel
import com.slaviboy.composeunits.dh

@Composable
fun LandingUi(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    landingViewModel: LandingViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        landingViewModel.checkAuthentication {
            Log.v("LandingUi", "checkAuthentication: Authenticated, navigating to MainUi")
            viewModel.navController.navigate(AppRoutes.Main.route) {
                popUpTo(AppRoutes.Landing.route) { inclusive = true }
            }
        }
    }

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
        )

        onDispose {}
    }

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                landingViewModel.onGoogleSignIn(credentials)

                viewModel.navController.navigate(AppRoutes.Main.route) {
                    popUpTo(AppRoutes.Landing.route) { inclusive = true }
                }
            } catch (it: ApiException) {
                Log.w("LandingUi", "Google sign in failed", it)
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

        Spacer(modifier = Modifier.size(0.1.dh))

        FilledTonalButton(
            onClick = {
                viewModel.navController.navigate(AppRoutes.SignIn.route)
            }
        ) {
            Text(
                text = stringResource(id = R.string.sign_in_or_sign_up),
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.size(0.01.dh))

        FilledTonalButton(
            modifier = Modifier.padding(8.dp),
            onClick = {
                val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(Constants.WEB_CLIENT_ID)
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)

                launcher.launch(googleSignInClient.signInIntent)
            },
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(id = R.string.sign_in),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        TextButton(
            onClick = {
                landingViewModel.onSkip {
                    viewModel.navController.navigate(AppRoutes.Main.route) {
                        popUpTo(AppRoutes.Landing.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.padding(8.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = stringResource(id = R.string.anonymous_sign_in))
        }
    }
}
