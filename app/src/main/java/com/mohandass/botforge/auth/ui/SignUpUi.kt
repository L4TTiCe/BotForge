// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.mohandass.botforge.auth.viewmodel.SignUpViewModel
import com.slaviboy.composeunits.adh
import com.mohandass.botforge.R.string as AppText

// Sign Up Using Email and Password
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpUi(
    appViewModel: AppViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
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
            text = stringResource(id = AppText.sign_up),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.size(0.02.adh))

        OutlinedTextField(
            value = signUpViewModel.email,
            onValueChange = { signUpViewModel.onEmailChange(it) },
            label = { Text(resources().getString(AppText.email)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )
        Spacer(modifier = Modifier.size(0.02.adh))
        OutlinedTextField(
            value = signUpViewModel.password,
            onValueChange = { signUpViewModel.onPasswordChange(it) },
            label = { Text(resources().getString(AppText.password)) },
            visualTransformation = if (signUpViewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (signUpViewModel.passwordVisibility) {
                    IconButton(onClick = { signUpViewModel.onPasswordVisibilityChange(false) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.hide_eye),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )
                    }
                } else {
                    IconButton(onClick = { signUpViewModel.onPasswordVisibilityChange(true) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.show_eye),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            )
        )
        Spacer(modifier = Modifier.size(0.01.adh))
        OutlinedTextField(
            value = signUpViewModel.confirmPassword,
            onValueChange = { signUpViewModel.onConfirmPasswordChange(it) },
            visualTransformation = if (signUpViewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text(resources().getString(AppText.confirm_password)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(modifier = Modifier.size(0.03.adh))

        FilledTonalButton(
            onClick = {
                signUpViewModel.onSignUp {
                    appViewModel.appState.navController.navigate(AppRoutes.Main.route) {
                        popUpTo(AppRoutes.SignUp.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = AppText.sign_up),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.size(0.01.adh))
        TextButton(
            onClick = {
                appViewModel.appState.navController.navigate(AppRoutes.SignIn.route) {
                    popUpTo(AppRoutes.SignUp.route) { inclusive = true }
                }
            },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = AppText.sign_in),
                modifier = Modifier.padding(4.dp)
            )
        }
        TextButton(
            onClick = {
                signUpViewModel.onEmailChange("")
                signUpViewModel.onPasswordChange("")
                signUpViewModel.onConfirmPasswordChange("")
            },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = AppText.reset),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
