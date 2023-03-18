package com.mohandass.botforge.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.resources
import com.mohandass.botforge.ui.settings.components.SettingsCategory
import com.mohandass.botforge.ui.settings.components.SettingsItem
import com.mohandass.botforge.viewmodels.AppViewModel
import com.mohandass.botforge.viewmodels.SettingsViewModel

@Composable
fun SettingsUi(
    viewModel: AppViewModel,
    settingsViewModel: SettingsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.topBar.title.value = R.string.settings
        viewModel.topBar.overrideMenu.value = true
        viewModel.topBar.menu.value = {
            IconButton(onClick = {
                viewModel.navControllerMain.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.back_cd)
                )
            }
        }
    }
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .fillMaxSize(),
    ) {
        Row {
            Text(
                text = resources().getString(R.string.settings),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        VersionInfo()

        Spacer(modifier = Modifier.height(10.dp))

        SettingsCategory(title = resources().getString(R.string.display))

        AppearanceSettings(
            viewModel = viewModel,
            settingsViewModel = settingsViewModel
        )

        Spacer(modifier = Modifier.height(10.dp))

        SettingsCategory(title = resources().getString(R.string.api))
        
        SettingsItem(
            title = resources().getString(R.string.api_key),
            description = resources().getString(R.string.api_settings_message),
            painter = painterResource(id = R.drawable.baseline_key_24),
            onClick = ({
                viewModel.navigateTo(AppRoutes.MainRoutes.ApiKeySettings.route)
            })
        )

        SettingsItem(
            title = resources().getString(R.string.usage_settings),
            description = resources().getString(R.string.usage_settings_message),
            painter = painterResource(id = R.drawable.baseline_token_24),
            onClick = ({
                viewModel.navigateTo(AppRoutes.MainRoutes.ApiUsageSettings.route)
            })
        )

        SettingsCategory(title = resources().getString(R.string.account))

        SettingsItem(
            title = resources().getString(R.string.manage_account),
            description = resources().getString(R.string.manage_account_message),
            painter = painterResource(id = R.drawable.baseline_manage_accounts_24),
            onClick = ({
                viewModel.navigateTo(AppRoutes.MainRoutes.ManageAccountSettings.route)
            })
        )
    }
}
