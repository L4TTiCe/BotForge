// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.slaviboy.composeunits.dw

@Composable
fun MessagesHeader(
    onExportClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onClearAllClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.messages),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onExportClick,
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_data_object_24
                ),
                contentDescription = stringResource(id = R.string.export_chat_cd),
                modifier = Modifier.size(24.dp)
            )
        }

        IconButton(
            onClick = onBookmarkClick,
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_bookmark_add_24
                ),
                contentDescription = stringResource(id = R.string.add_to_bookmarks_cd),
                modifier = Modifier.size(24.dp)
            )
        }

        IconButton(
            onClick = onClearAllClick,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_clear_all_24
                ),
                contentDescription = stringResource(id = R.string.clear_all_cd),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(0.01.dw))
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesHeaderPreview() {
    MessagesHeader()
}
