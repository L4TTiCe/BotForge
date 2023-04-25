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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.ui.ShakeWithHaptic
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel
import com.mohandass.botforge.sync.viewmodel.BrowseViewModel
import com.slaviboy.composeunits.adh

@Composable
fun SettingsUi(
    appViewModel: AppViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    browseViewModel: BrowseViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        appViewModel.appState.topBar.title.value = R.string.settings
        appViewModel.appState.topBar.overrideMenu.value = true
        appViewModel.appState.topBar.menu.value = {
            IconButton(onClick = {
                appViewModel.appState.navControllerMain.popBackStack()
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
    val isAnalyticsEnabled = remember {
        mutableStateOf(settingsViewModel.isAnalyticsEnabled)
    }
    val isAutoChatNameEnabled = remember {
        mutableStateOf(true)
    }
    val isImageGenerationEnabled = remember {
        mutableStateOf(true)
    }
    var shakeSensitivity by remember { mutableStateOf(0f) }

    var statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    val userPreferences = appViewModel.appState.userPreferences.observeAsState()
    userPreferences.value?.let {
        statusBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
//        navigationBarColor = MaterialTheme.colorScheme.background

        activeTheme = it.preferredTheme
        useDynamicColor = it.useDynamicColors
        isUserGeneratedContentEnabled.value = it.enableUserGeneratedContent
        isShakeToClearEnabled.value = it.enableShakeToClear
        shakeSensitivity = it.shakeToClearSensitivity
        isAutoChatNameEnabled.value = it.autoGenerateChatTitle
        isImageGenerationEnabled.value = it.enableImageGeneration
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
            AppearanceSettings()

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
                    appViewModel.appState.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
                })
            )
        }
        item{
            SettingsItem(
                title = "Enable Image Generation",
                description = "Allows generating Images using the API",
                icon = painterResource(id = R.drawable.picture),
                switchState = isImageGenerationEnabled,
                onCheckChange = {
                    settingsViewModel.updateImageGenerationEnabled(it)
                }
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.usage_settings),
                description = resources().getString(R.string.usage_settings_message),
                painter = painterResource(id = R.drawable.baseline_token_24),
                onClick = ({
                    appViewModel.appState.navControllerMain.navigate(AppRoutes.MainRoutes.ApiUsageSettings.route)
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
                    appViewModel.appState.navControllerMain.navigate(AppRoutes.MainRoutes.ManageAccountSettings.route)
                })
            )
        }
        item {
            SettingsCategory(title = resources().getString(R.string.chat))
        }
        item {
            SettingsItem(
                title = "Auto-Generate Chat Title",
                description = "Automatically generates Title when saving Chats, uses OpenAI API",
                icon = painterResource(id = R.drawable.baseline_title_24),
                switchState = isAutoChatNameEnabled,
                onCheckChange = {
                    settingsViewModel.setAutoGenerateChatTitle(it)
                }
            )

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
                ShakeWithHaptic(shakeSensitivity = shakeSensitivity) {
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
                    browseViewModel.deleteAllBots()
                    settingsViewModel.clearSyncInfo()
                })
            )
        }

        item {
            SettingsCategory(title = resources().getString(R.string.analytics))
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.enable_analytics),
                description = resources().getString(R.string.enable_analytics_message),
                icon = painterResource(id = R.drawable.outline_analytics_24),
                switchState = isAnalyticsEnabled,
                onCheckChange = {
                    settingsViewModel.setAnalyticsEnabled(it)
                    isAnalyticsEnabled.value = it
                }
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
                    appViewModel.appState.navControllerMain.navigate(AppRoutes.MainRoutes.OpenSourceLicenses.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.icons_images),
                description = resources().getString(R.string.icons_images_message),
                painter = painterResource(id = R.drawable.tag_black_shape),
                onClick = ({
                    appViewModel.appState.navControllerMain.navigate(AppRoutes.MainRoutes.IconCredits.route)
                })
            )
        }
        item {
            SettingsItem(
                title = resources().getString(R.string.app_information),
                description = resources().getString(R.string.app_information_message),
                painter = painterResource(id = R.drawable.baseline_info_24),
                onClick = ({
                    appViewModel.appState.navControllerMain.navigate(AppRoutes.MainRoutes.AppInformation.route)
                })
            )
        }
        item {
            Spacer(modifier = Modifier.height(0.1.adh))
        }
    }
}
