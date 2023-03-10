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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.resources
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.ui.viewmodels.AppViewModel
import com.mohandass.botforge.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsUi(
    viewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
    appState: AppState? = null
) {
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

            IconButton(onClick = {
                viewModel.navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.back_cd)
                )
            }
        }

        VersionInfo()

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ApiSettings()

        Spacer(modifier = Modifier.height(10.dp))

        AccountSettings(viewModel = viewModel, settingsViewModel = settingsViewModel, appState = appState)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsUiPreview() {
    BotForgeTheme {
        SettingsUi(viewModel = hiltViewModel(), settingsViewModel = hiltViewModel())
    }
}