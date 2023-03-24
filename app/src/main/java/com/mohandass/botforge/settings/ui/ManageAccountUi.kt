package com.mohandass.botforge.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.auth.model.User
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel

@Composable
fun ManageAccountUi(
    viewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
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
                        viewModel.navigateTo(AppRoutes.Landing.route)
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

        if (user.isAnonymous) {
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

        Spacer(modifier = Modifier.height(10.dp))

        SettingsCategory(title = stringResource(id = R.string.account_actions),)

        if (user.isAnonymous) {
            SettingsItem(
                title = stringResource(id = R.string.link_account),
                description = stringResource(id = R.string.link_account_message),
                painter = painterResource(id = R.drawable.baseline_add_link_24),
            ) {
                SnackbarManager.showMessage(R.string.not_implemented)
            }
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