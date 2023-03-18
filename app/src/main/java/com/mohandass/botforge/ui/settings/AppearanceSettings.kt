package com.mohandass.botforge.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.model.preferences.PreferredTheme
import com.mohandass.botforge.viewmodels.AppViewModel
import com.mohandass.botforge.viewmodels.SettingsViewModel

@Composable
fun AppearanceSettings(viewModel: AppViewModel, settingsViewModel: SettingsViewModel) {
    var activeTheme = remember {
        PreferredTheme.AUTO
    }
    var useDynamicColors = remember {
        true
    }

    val userPreferences = viewModel.userPreferences.observeAsState()
    userPreferences.value?.let {
        activeTheme = it.preferredTheme
        useDynamicColors = it.useDynamicColors
    }

    Column {
        Text(
            text = "Appearance",
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { settingsViewModel.updateTheme(PreferredTheme.LIGHT) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_light_mode_24),
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.back_cd),
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
                    contentDescription = stringResource(id = R.string.back_cd),
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
                    contentDescription = stringResource(id = R.string.back_cd),
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
                text = "Light",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Auto",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Dark",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(
                text = "Use Dynamic Colors",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = useDynamicColors,
                onCheckedChange = { settingsViewModel.updateDynamicColor(it) }
            )

//            Checkbox(
//                checked = useDynamicColors,
//                onCheckedChange = { settingsViewModel.updateDynamicColor(it) }
//            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}