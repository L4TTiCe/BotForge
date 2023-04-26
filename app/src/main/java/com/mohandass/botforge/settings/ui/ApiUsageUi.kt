// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.mohandass.botforge.settings.ui.components.dialogs.TokenInfoDialog
import com.mohandass.botforge.settings.viewmodel.SettingsViewModel
import com.slaviboy.composeunits.adw
import java.text.DecimalFormat

@Composable
fun ApiUsageUi(
    settingsViewModel: SettingsViewModel,
) {
    val showTokenInfoDialog = remember { mutableStateOf(false) }

    val usageTokens by settingsViewModel.usageTokens
    val usageImageSmallCount by settingsViewModel.usageImageSmallCount
    val usageImageMediumCount by settingsViewModel.usageImageMediumCount
    val usageImageLargeCount by settingsViewModel.usageImageLargeCount

    val context = LocalContext.current

    if (showTokenInfoDialog.value) {
        TokenInfoDialog(
            onDismiss = { showTokenInfoDialog.value = false },
            context = context
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = resources().getString(R.string.api_usage),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = resources().getString(R.string.usage_quotas),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = resources().getString(R.string.usage_quotas_message),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = resources().getString(R.string.your_usage),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(0.05.adw))

            Text(
                text = stringResource(R.string.tokens),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.labelLarge
            )

            IconButton(onClick = {
                showTokenInfoDialog.value = true
            }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(id = R.string.info_cd),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_token_24),
                        contentDescription = stringResource(id = R.string.tokens),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = usageTokens.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(0.05.adw))
                }

                // Show cost only if API level is 29+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_money_24),
                            contentDescription = stringResource(id = R.string.dollars),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = DecimalFormat("0.0000")
                                .format(
                                    resources().getFloat(R.dimen.gpt_3_5_turbo_cost_per_1k_tokens) *
                                            usageTokens.div(1000)
                                ).toString(),
                            modifier = Modifier.padding(end = 10.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(0.05.adw))

            Text(
                text = stringResource(id = R.string.image_generation),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(0.1.adw))

            Text(
                text = stringResource(id = R.string.image_small),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = usageImageSmallCount.toString(),
                modifier = Modifier.padding(end = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(0.05.adw))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(0.1.adw))

            Text(
                text = stringResource(id = R.string.image_medium),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = usageImageMediumCount.toString(),
                modifier = Modifier.padding(end = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(0.05.adw))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(0.1.adw))

            Text(
                text = stringResource(id = R.string.image_large),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = usageImageLargeCount.toString(),
                modifier = Modifier.padding(end = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(0.05.adw))
        }

        // Show cost only if API level is 29+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_attach_money_24),
                    contentDescription = stringResource(id = R.string.dollars),
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = DecimalFormat("0.0000")
                        .format(
                            (resources().getFloat(R.dimen.dall_e_256) * usageImageSmallCount) +
                                    (resources().getFloat(R.dimen.dall_e_512) * usageImageMediumCount) +
                                    (resources().getFloat(R.dimen.dall_e_1024) * usageImageLargeCount)
                        ).toString(),
                    modifier = Modifier.padding(end = 10.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.width(10.dp))
            }
        }

        Row {
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                modifier = Modifier.padding(horizontal = 10.dp),
                onClick = {
                    settingsViewModel.resetUsage()
                }
            ) {
                Text(
                    text = resources().getString(R.string.reset_usage),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(modifier = Modifier.width(5.dp))
        }

        SettingsCategory(title = stringResource(id = R.string.external_links))

        SettingsItem(
            title = resources().getString(R.string.usage_summary),
            description = resources().getString(R.string.usage_summary_message),
            painter = painterResource(id = R.drawable.baseline_data_usage_24),
        ) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(context.getString(R.string.open_ai_usage_link))
            }
            context.startActivity(intent)
        }

        SettingsItem(
            title = resources().getString(R.string.open_ai_pricing),
            description = resources().getString(R.string.open_ai_pricing_message),
            painter = painterResource(id = R.drawable.baseline_price_check_24),
        ) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(context.getString(R.string.open_ai_pricing_link))
            }
            context.startActivity(intent)
        }
    }
}
