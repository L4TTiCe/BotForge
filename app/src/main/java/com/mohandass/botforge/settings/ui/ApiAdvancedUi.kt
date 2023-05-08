// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.ui.components.DropdownButton
import com.mohandass.botforge.image.ui.components.NumberPicker
import com.mohandass.botforge.settings.model.internal.ModelInternal
import com.mohandass.botforge.settings.viewmodel.AdvancedApiSettingsViewModel

@Composable
fun ApiAdvancedUi(
    appViewModel: AppViewModel = hiltViewModel(),
    advancedApiSettingsViewModel: AdvancedApiSettingsViewModel = hiltViewModel(),
) {
    var apiTimeout by remember { advancedApiSettingsViewModel.apiTimeout }

    var selectedModel by remember { advancedApiSettingsViewModel.selectedModel }
    val availableModels by remember { advancedApiSettingsViewModel.availableModels }

    LaunchedEffect(Unit) {
        advancedApiSettingsViewModel.getAvailableModels()
    }

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

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.advanced_api_settings),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.model),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(id = R.string.model_description),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        DropdownButton<ModelInternal>(
            options = availableModels,
            selectedOption = selectedModel,
            onOptionSelected = { model ->
                selectedModel = model.model.id.id
                advancedApiSettingsViewModel.setChatModel()
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.api_timeout),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(id = R.string.api_timeout_description),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(10.dp))

        NumberPicker(
            numberAsString = "$apiTimeout s",
            onIncrement = {
                if (apiTimeout < Constants.MAX_API_TIMEOUT) {
                    apiTimeout++
                    advancedApiSettingsViewModel.setApiTimeout()
                }
            },
            onDecrement = {
                if (apiTimeout > Constants.MIN_API_TIMEOUT) {
                    apiTimeout--
                    advancedApiSettingsViewModel.setApiTimeout()
                }
            },
        )
    }
}
