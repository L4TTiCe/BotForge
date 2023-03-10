package com.mohandass.botforge.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.resources

@Composable
fun VersionInfo() {
    Text(
        text = resources().getString(R.string.version, Utils.getAppVersion()),
        modifier = Modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.bodySmall
    )
    Text(
        text = resources().getString(R.string.build, Utils.getAppVersionCode().toString()),
        modifier = Modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.bodySmall
    )
}
