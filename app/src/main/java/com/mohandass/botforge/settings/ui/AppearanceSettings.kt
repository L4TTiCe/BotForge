// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.ui

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.model.PreferredHeader
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel

@Composable
fun AppearanceSettings(
    appViewModel: AppViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    var activeTheme = remember {
        PreferredTheme.AUTO
    }
    val useDynamicColors = remember {
        mutableStateOf(false)
    }
    val preferMinimalHeader = remember {
        mutableStateOf(false)
    }

    val userPreferences = appViewModel.appState.userPreferences.observeAsState()
    userPreferences.value?.let {
        activeTheme = it.preferredTheme
        useDynamicColors.value = it.useDynamicColors
        preferMinimalHeader.value = it.preferredHeader == PreferredHeader.MINIMAL_HEADER
    }

    Column {
        Text(
            text = resources().getString(R.string.appearance_settings),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { settingsViewModel.updateTheme(PreferredTheme.LIGHT) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_light_mode_24),
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.light_mode_cd),
                    tint =
                    if (activeTheme == PreferredTheme.LIGHT)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { settingsViewModel.updateTheme(PreferredTheme.AUTO) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_brightness_auto_24),
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.system_default_cd),
                    tint =
                    if (activeTheme == PreferredTheme.AUTO)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onBackground
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { settingsViewModel.updateTheme(PreferredTheme.DARK) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_dark_mode_24),
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.dark_mode_cd),
                    tint =
                    if (activeTheme == PreferredTheme.DARK)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = resources().getString(R.string.light_mode),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = resources().getString(R.string.system_default),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = resources().getString(R.string.dark_mode),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            Spacer(modifier = Modifier.height(10.dp))

            SettingsItem(
                title = resources().getString(R.string.dynamic_colors),
                description = resources().getString(R.string.dynamic_colors_message),
                icon = painterResource(id = R.drawable.baseline_color_lens_24),
                switchState = useDynamicColors,
                onCheckChange = { settingsViewModel.updateDynamicColor(it) }
            )

        }

        Spacer(modifier = Modifier.height(10.dp))

        SettingsItem(
            title = resources().getString(R.string.use_minimal_header),
            description = resources().getString(R.string.use_minimal_header_message),
            icon = painterResource(id = R.drawable.baseline_view_headline_24),
            switchState = preferMinimalHeader,
            onCheckChange = {
                settingsViewModel.updatePreferredHeader(
                    if (it) PreferredHeader.MINIMAL_HEADER else PreferredHeader.DEFAULT_HEADER
                )
            }
        )
    }
}