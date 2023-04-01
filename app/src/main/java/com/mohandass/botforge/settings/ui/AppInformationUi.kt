package com.mohandass.botforge.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.slaviboy.composeunits.dh

@Composable
fun AppInformationUi() {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        item {
            Text(
                text = resources().getString(R.string.app_information),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    tint = Color.Unspecified
                )

                Text(
                    text = resources().getString(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = resources().getString(R.string.about_app),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            SettingsItem(
                title = resources().getString(R.string.version_number),
                description = Utils.getAppVersion(),
                painter = painterResource(id = R.drawable.baseline_info_24),
            ) {}
        }

        item {
            SettingsItem(
                title = resources().getString(R.string.build_number),
                description = Utils.getAppVersionCode().toString(),
                painter = painterResource(id = R.drawable.baseline_numbers_24),
            ) {}
        }

        item {
            SettingsItem(
                title = resources().getString(R.string.play_store),
                description = resources().getString(R.string.play_store_message),
                painter = painterResource(id = R.drawable.play_store_ic),
            ) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(context.getString(R.string.play_store_link))
                }
                context.startActivity(intent)
            }
        }

        item {
            SettingsItem(
                title = resources().getString(R.string.github),
                description = resources().getString(R.string.github_message),
                painter = painterResource(id = R.drawable.baseline_bug_report_24),
            ) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(context.getString(R.string.github_link))
                }
                context.startActivity(intent)
            }
        }

        item {
            SettingsItem(
                title = resources().getString(R.string.privacy_policy),
                description = resources().getString(R.string.privacy_policy_message),
                painter = painterResource(id = R.drawable.baseline_policy_24),
            ) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(context.getString(R.string.privacy_policy_link))
                }
                context.startActivity(intent)
            }
        }

        item {
            Spacer(modifier = Modifier.height(0.2.dh))
        }

    }
}