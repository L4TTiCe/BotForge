// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.ui.settings

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.Constants
import com.mohandass.botforge.auth.model.User
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel

@Composable
fun ManageAccountUi(
    viewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val user by settingsViewModel.getCurrentUser().collectAsState(User())

    var displayName by remember { mutableStateOf(settingsViewModel.getDisplayName()) }
    var isAnonymous by remember {
        mutableStateOf(user.isAnonymous)
    }

    user.let {
        displayName = it.displayName.toString()
        isAnonymous = it.isAnonymous
    }

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                settingsViewModel.onGoogleSignIn(credentials) {
                    isAnonymous = false
                }
            } catch (it: ApiException) {
                Log.e("GoogleSignIn", "Google sign in failed", it)
            }
        }

    val openDeleteDialog = remember { mutableStateOf(false) }

    if (openDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { openDeleteDialog.value = false },
            title = {
                Text(text = stringResource(id = R.string.delete_account))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_account_message))
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAccount {
                        viewModel.navController.navigate(AppRoutes.Landing.route)
                        openDeleteDialog.value = false
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDeleteDialog.value = false
                }) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.errorContainer,
            textContentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.account),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleLarge
        )

        if (isAnonymous) {
            Text(
                text = stringResource(id = R.string.logged_in_as_anonymous),
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = stringResource(id = R.string.uid, user.id),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Display Name: $displayName",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        SettingsCategory(title = stringResource(id = R.string.account_actions))

        if (isAnonymous) {
            SettingsItem(
                title = stringResource(id = R.string.link_account),
                description = stringResource(id = R.string.link_account_message),
                painter = painterResource(id = R.drawable.baseline_add_link_24),
            ) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(Constants.WEB_CLIENT_ID)
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)

                launcher.launch(googleSignInClient.signInIntent)
            }
        }

        SettingsItem(
            title = "Regenerate Display Name",
            description = "This will generate a new display name for your account",
            painter = painterResource(id = R.drawable.dice),
        ) {
            settingsViewModel.regenerateDisplayName()
            displayName = settingsViewModel.getDisplayName()
        }

        SettingsItem(
            title = stringResource(id = R.string.sign_out),
            description = stringResource(id = R.string.sign_out_message),
            painter = painterResource(id = R.drawable.baseline_logout_24),
        ) {
            viewModel.signOut {
                viewModel.navController.navigate(AppRoutes.Landing.route)
            }
        }

        SettingsItem(
            title = stringResource(id = R.string.delete_account),
            description = stringResource(id = R.string.delete_account_message_2),
            painter = painterResource(id = R.drawable.baseline_delete_24),
        ) {
            openDeleteDialog.value = true
        }
    }
}