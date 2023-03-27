package com.mohandass.botforge.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.R
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel
import com.slaviboy.composeunits.dh

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

    var activeTheme = remember {
        PreferredTheme.AUTO
    }
    var useDynamicColor = remember {
        true
    }

    var statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
//    var navigationBarColor = MaterialTheme.colorScheme.background

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    val userPreferences = viewModel.userPreferences.observeAsState()
    userPreferences.value?.let {
        statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
//        navigationBarColor = MaterialTheme.colorScheme.background

        activeTheme = it.preferredTheme
        useDynamicColor = it.useDynamicColors


    }

    DisposableEffect(systemUiController, activeTheme, useDynamicColor) {

        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = when (activeTheme) {
                PreferredTheme.AUTO -> useDarkIcons
                PreferredTheme.LIGHT -> true
                PreferredTheme.DARK -> false
            }
        )

        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = when (activeTheme) {
                PreferredTheme.AUTO -> useDarkIcons
                PreferredTheme.LIGHT -> true
                PreferredTheme.DARK -> false
            }
        )

        onDispose {}
    }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
    ) {
        item {
            Row {
                Text(
                    text = resources().getString(R.string.settings),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
        item {
            SettingsCategory(title = resources().getString(R.string.display))
        }
        item {
            AppearanceSettings(
                viewModel = viewModel,
                settingsViewModel = settingsViewModel
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            SettingsCategory(title = resources().getString(R.string.api))
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.api_key),
                description = resources().getString(R.string.api_settings_message),
                painter = painterResource(id = R.drawable.baseline_key_24),
                onClick = ({
                    viewModel.navigateTo(AppRoutes.MainRoutes.ApiKeySettings.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.usage_settings),
                description = resources().getString(R.string.usage_settings_message),
                painter = painterResource(id = R.drawable.baseline_token_24),
                onClick = ({
                    viewModel.navigateTo(AppRoutes.MainRoutes.ApiUsageSettings.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.open_ai_status),
                description = resources().getString(R.string.open_ai_status_message),
                painter = painterResource(id = R.drawable.baseline_open_in_new_24),
                onClick = ({
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(context.getString(R.string.open_ai_status_link))
                    }
                    context.startActivity(intent)
                })
            )
        }
        item {
            SettingsCategory(title = resources().getString(R.string.account))
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.manage_account),
                description = resources().getString(R.string.manage_account_message),
                painter = painterResource(id = R.drawable.baseline_manage_accounts_24),
                onClick = ({
                    viewModel.navigateTo(AppRoutes.MainRoutes.ManageAccountSettings.route)
                })
            )
        }
        item {
            SettingsCategory(title = resources().getString(R.string.community))
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.clear_community_cache),
                description = resources().getString(R.string.clear_community_cache_message),
                painter = painterResource(id = R.drawable.baseline_delete_24),
                onClick = ({
                    viewModel.browse.deleteAllBots()
                    settingsViewModel.clearLastSyncTime()
                })
            )
        }
        item {
            SettingsCategory(title = resources().getString(R.string.about))
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.licenses_and_acknowledgements),
                description = resources().getString(R.string.licenses_and_acknowledgements_message),
                painter = painterResource(id = R.drawable.baseline_library_books_24),
                onClick = ({
                    viewModel.navigateTo(AppRoutes.MainRoutes.OpenSourceLicenses.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.app_information),
                description = resources().getString(R.string.app_information_message),
                painter = painterResource(id = R.drawable.baseline_info_24),
                onClick = ({
                    viewModel.navigateTo(AppRoutes.MainRoutes.AppInformation.route)
                })
            )
        }
        item {
            Spacer(modifier = Modifier.height(0.1.dh))
        }
    }
}
