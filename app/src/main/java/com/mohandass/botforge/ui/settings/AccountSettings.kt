package com.mohandass.botforge.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.model.User
import com.mohandass.botforge.ui.viewmodels.AppViewModel
import com.mohandass.botforge.ui.viewmodels.SettingsViewModel

@Composable
fun AccountSettings(
    viewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
    appState: AppState? = null
) {
    val user by settingsViewModel.getCurrentUser().collectAsState(User())
    val openDeleteDialog = remember { mutableStateOf(false) }

    if (openDeleteDialog.value) {
        AlertDialog(onDismissRequest = { openDeleteDialog.value = false },
            title = {
                Text(text = stringResource(id = R.string.delete_account))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_account_message))
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAccount {
                        appState?.navigateTo(AppRoutes.Landing.route)
                        openDeleteDialog.value = false
                        SnackbarManager.showMessage(R.string.delete_account_success)
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

    Text(
        text = "Account",
        modifier = Modifier.padding(10.dp),
        style = MaterialTheme.typography.titleMedium
    )

    if (user.isAnonymous) {
        Text(
            text = "You have logged in anonymously",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }

    Text(
        text = "UID:\t${user.id}",
        modifier = Modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.bodySmall
    )

    Row(modifier = Modifier.padding(10.dp)) {
        OutlinedButton(onClick = { /*TODO*/
            SnackbarManager.showMessage(R.string.not_implemented)
        }) {
            Text(text = "Link Account")
        }
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedButton(onClick = {
            viewModel.signOut {
                appState?.navigateTo(AppRoutes.Landing.route)
            }
        }) {
            Text(text = "Sign Out")
        }
    }
    OutlinedButton(
        modifier = Modifier.padding(horizontal = 10.dp),
        onClick = {
            openDeleteDialog.value = true
        },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        ),
    ) {
        Text(text = "Delete Account")
    }
}
