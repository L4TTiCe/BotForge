package com.mohandass.botforge.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.Screen
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.ui.viewmodels.SignUpViewModel
import com.slaviboy.composeunits.dh
import com.mohandass.botforge.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpUi(appState: AppState?, viewModel: SignUpViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.size(0.10.dh))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(0.15.dh),
            contentScale = ContentScale.Inside,
        )

        Spacer(modifier = Modifier.size(0.05.dh))

        Text(
            text = stringResource(id = AppText.sign_up),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.size(0.02.dh))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )
        Spacer(modifier = Modifier.size(0.02.dh))
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            visualTransformation = if (viewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (viewModel.passwordVisibility) {
                    IconButton(onClick = { viewModel.onPasswordVisibilityChange(false) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.hide_eye),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )
                    }
                } else {
                    IconButton(onClick = { viewModel.onPasswordVisibilityChange(true) }) {
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
        Spacer(modifier = Modifier.size(0.01.dh))
        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            visualTransformation = if (viewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(modifier = Modifier.size(0.03.dh))

        FilledTonalButton(
            onClick = { },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = AppText.sign_up),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.size(0.01.dh))
        TextButton(
            onClick = {
                appState?.navController?.navigate(Screen.SignIn.route) {
                    popUpTo(Screen.SignUp.route) { inclusive = true }
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
                viewModel.onEmailChange("")
                viewModel.onPasswordChange("")
                viewModel.onConfirmPasswordChange("")
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

@Preview(showBackground = true)
@Composable
fun SignUpUiPreview() {
    BotForgeTheme {
        SignUpUi(appState = null)
    }
}