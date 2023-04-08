// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyUi(
    viewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val apiKey = remember { mutableStateOf(settingsViewModel.getApiKey()) }
    val scrollState = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = resources().getString(R.string.api_key),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        modifier = Modifier
                            .size(16.dp),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = resources().getString(R.string.caution),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = resources().getString(R.string.api_key_warning_1),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = resources().getString(R.string.api_key_warning_2),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = resources().getString(R.string.api_key),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.labelMedium,
        )

        OutlinedTextField(
            value = apiKey.value,
            onValueChange = {
                apiKey.value = it
            },
            placeholder = {
                Text(
                    text = resources().getString(R.string.api_key_hint),
                )
            },
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 2.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    settingsViewModel.setApiKey(apiKey.value)
                    keyboardController?.hide()
                }
            )
        )

        Text(
            text = resources().getString(
                R.string.api_key_info,
                resources().getString(R.string.api_key_link)
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.bodySmall,
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row {
            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                modifier = Modifier.padding(horizontal = 10.dp),
                onClick = {
                    settingsViewModel.setApiKey(apiKey.value)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ) {
                Text(text = resources().getString(R.string.save))
            }
        }

        SettingsCategory(title = resources().getString(R.string.external_links))

        SettingsItem(
            title = resources().getString(R.string.open_ai_api_website),
            description = resources().getString(R.string.open_ai_api_website_message),
            painter = painterResource(id = R.drawable.baseline_key_24),
        ) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(context.getString(R.string.api_key_link))
            }
            context.startActivity(intent)
        }
    }
}