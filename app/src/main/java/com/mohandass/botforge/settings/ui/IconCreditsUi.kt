package com.mohandass.botforge.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.resources
import com.mohandass.botforge.settings.ui.components.SettingsCategory
import com.mohandass.botforge.settings.ui.components.SettingsItem
import com.slaviboy.composeunits.adh

@Composable
fun IconCreditsUi() {
    val context = LocalContext.current

    val attributionInfo = resources().getStringArray(R.array.attribution_info)
    val attributionIcons = resources().obtainTypedArray(R.array.attribution_icons)

    DisposableEffect(Unit) {
        onDispose {
            attributionIcons.recycle()
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
    ) {
        item {
            SettingsCategory(
                title = "Icons",
            )
        }

        itemsIndexed(attributionInfo) { index, info ->
            val (name, description, link) = info.split(", ")
            val iconId = attributionIcons.getResourceId(index, 0)

            SettingsItem(
                title = name,
                description = description,
                painter = painterResource(id = iconId),
                onClick = ({
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(link)
                    }
                    context.startActivity(intent)
                })
            )
        }

        item {
            Spacer(modifier = Modifier.padding(0.1.adh))
        }
    }
}
