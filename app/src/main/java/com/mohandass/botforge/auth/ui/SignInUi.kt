package com.mohandass.botforge.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.*
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.viewmodel.SignInViewModel
import com.slaviboy.composeunits.adh
import com.mohandass.botforge.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInUi(viewModel: AppViewModel, signInViewModel: SignInViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.size(0.10.adh))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(0.15.adh),
            contentScale = ContentScale.Inside,
        )

        Spacer(modifier = Modifier.size(0.05.adh))

        Text(
            text = stringResource(id = AppText.sign_in),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.size(0.02.adh))

        OutlinedTextField(
            value = signInViewModel.email,
            onValueChange = { signInViewModel.onEmailChange(it) },
            label = { Text(resources().getString(AppText.email)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )
        Spacer(modifier = Modifier.size(0.02.adh))
        OutlinedTextField(
            value = signInViewModel.password,
            onValueChange = { signInViewModel.onPasswordChange(it) },
            label = { Text(resources().getString(AppText.password)) },
            visualTransformation = if (signInViewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (signInViewModel.passwordVisibility) {
                    IconButton(onClick = { signInViewModel.onPasswordVisibilityChange(false) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.hide_eye),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )
                    }
                } else {
                    IconButton(onClick = { signInViewModel.onPasswordVisibilityChange(true) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.show_eye),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(modifier = Modifier.size(0.12.adh))

        FilledTonalButton(
            onClick = {
                signInViewModel.signIn {
                    viewModel.navController.navigate(AppRoutes.Main.route) {
                        popUpTo(AppRoutes.SignIn.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = AppText.sign_in),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.size(0.01.adh))
        TextButton(
            onClick = {
                viewModel.navController.navigate(AppRoutes.SignUp.route) {
                    popUpTo(AppRoutes.SignIn.route) { inclusive = true }
                }
            },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = AppText.sign_up),
                modifier = Modifier.padding(4.dp)
            )
        }
        TextButton(
            onClick = {
                signInViewModel.sendRecoveryEmail()
            },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = AppText.forgot_password),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
