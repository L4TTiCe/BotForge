// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.ui.ShakeDetector
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel
import com.mohandass.botforge.ui.settings.AppearanceSettings
import com.slaviboy.composeunits.adh

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

    var activeTheme by remember {
        mutableStateOf(PreferredTheme.AUTO)
    }
    var useDynamicColor by remember {
        mutableStateOf(true)
    }
    val isUserGeneratedContentEnabled = remember {
        mutableStateOf(true)
    }
    val isShakeToClearEnabled = remember {
        mutableStateOf(false)
    }
    var shakeSensitivity by remember { mutableStateOf(0f) }

    var statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    val userPreferences = viewModel.userPreferences.observeAsState()
    userPreferences.value?.let {
        statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
//        navigationBarColor = MaterialTheme.colorScheme.background

        activeTheme = it.preferredTheme
        useDynamicColor = it.useDynamicColors
        isUserGeneratedContentEnabled.value = it.enableUserGeneratedContent
        isShakeToClearEnabled.value = it.enableShakeToClear
        shakeSensitivity = it.shakeToClearSensitivity
    }

    DisposableEffect(
        systemUiController,
        activeTheme,
        useDynamicColor,
        isUserGeneratedContentEnabled
    ) {

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
                    viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.usage_settings),
                description = resources().getString(R.string.usage_settings_message),
                painter = painterResource(id = R.drawable.baseline_token_24),
                onClick = ({
                    viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.ApiUsageSettings.route)
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
                    viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.ManageAccountSettings.route)
                })
            )
        }
        item {
            SettingsCategory(title = resources().getString(R.string.chat))
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.enable_shake),
                description = resources().getString(R.string.enable_shake_message),
                icon = painterResource(id = R.drawable.phone_vibration),
                switchState = isShakeToClearEnabled,
                onCheckChange = {
                    settingsViewModel.setShakeToClear(it)
                }
            )

            if (isShakeToClearEnabled.value) {
                val shakeThreshold = remember(shakeSensitivity) {
                    val threshold = shakeSensitivity - (Constants.MAX_SENSITIVITY_THRESHOLD / 2)
                    (threshold * -1) + (Constants.MAX_SENSITIVITY_THRESHOLD / 2)
                }

                ShakeDetector(shakeThreshold = shakeThreshold) {
                    SnackbarManager.showMessage(R.string.shake_trigger_message)
                }
            }

            AnimatedVisibility(
                visible = isShakeToClearEnabled.value,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Row {
                        Text(
                            text = resources().getString(R.string.shake_sensitivity),
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.labelLarge
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = stringResource(id = R.string.shake_test),
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Slider(
                        value = shakeSensitivity,
                        onValueChange = {
                            shakeSensitivity = it
                        },
                        onValueChangeFinished = {
                            settingsViewModel.setShakeToClearSensitivity(shakeSensitivity)
                        },
                        valueRange = 0f..Constants.MAX_SENSITIVITY_THRESHOLD,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                }
            }
        }
        item {
            SettingsCategory(title = resources().getString(R.string.community))
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.enable_ugc),
                description = resources().getString(R.string.enable_ugc_message),
                icon = painterResource(id = R.drawable.baseline_color_lens_24),
                switchState = isUserGeneratedContentEnabled,
                onCheckChange = {
                    settingsViewModel.setUserGeneratedContent(it)
                }
            )
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
                    viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.OpenSourceLicenses.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.icons_images),
                description = resources().getString(R.string.icons_images_message),
                painter = painterResource(id = R.drawable.tag_black_shape),
                onClick = ({
                    viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.IconCredits.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.app_information),
                description = resources().getString(R.string.app_information_message),
                painter = painterResource(id = R.drawable.baseline_info_24),
                onClick = ({
                    viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.AppInformation.route)
                })
            )
        }
        item {
            Spacer(modifier = Modifier.height(0.1.adh))
        }
    }
}
