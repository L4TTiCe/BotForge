// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun HeaderWithActionIcon(
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: Painter,
    leadingIconContentDescription: String? = null,
    trailingIcon: Painter,
    trailingIconContentDescription: String? = null,
    trailingIconOnClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = leadingIcon,
            contentDescription = leadingIconContentDescription,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.weight(1f))

        // Clear all button
        IconButton(onClick = trailingIconOnClick) {
            Icon(
                painter = trailingIcon,
                contentDescription = trailingIconContentDescription,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
fun HeaderWithActionIconPreviewBookamrks() {
    HeaderWithActionIcon(
        text = stringResource(id = R.string.bookmarked),
        leadingIcon = painterResource(id = R.drawable.baseline_bookmarks_24),
        trailingIcon = painterResource(id = R.drawable.baseline_clear_all_24),
        trailingIconOnClick = {}
    )
}

@Preview
@Composable
fun HeaderWithActionIconPreviewList() {
    HeaderWithActionIcon(
        text = "Bot List",
        leadingIcon = painterResource(id = R.drawable.list),
        trailingIcon = painterResource(id = R.drawable.baseline_clear_all_24),
        trailingIconOnClick = {}
    )
}
