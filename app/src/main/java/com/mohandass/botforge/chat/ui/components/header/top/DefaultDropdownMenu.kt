// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header.top

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.dialogs.DeleteAllPersonasDialog
import com.mohandass.botforge.common.Constants
import com.slaviboy.composeunits.adh
import com.slaviboy.composeunits.adw

@Composable
fun DefaultDropdownMenu(viewModel: AppViewModel) {
    var displayOptionsMenu by remember { mutableStateOf(false) }
    val openDeleteConfirmationDialog = remember { mutableStateOf(false) }

    if (openDeleteConfirmationDialog.value) {
        DeleteAllPersonasDialog(
            onDismiss = { openDeleteConfirmationDialog.value = false },
            onConfirm = {
                viewModel.persona.deleteAllPersonas()
                viewModel.persona.clearSelection()
                openDeleteConfirmationDialog.value = false
            }
        )
    }

    IconButton(onClick = { displayOptionsMenu = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.more_options_cd),
            modifier = Modifier
                .size(Constants.ICONS_SIZE.dp)
        )
    }

    Spacer(modifier = Modifier.width(0.02.adw))

    DropdownMenu(
        expanded = displayOptionsMenu,
        onDismissRequest = { displayOptionsMenu = false },
        offset = DpOffset(0.8.adw, (0.01).adh),
    ) {
        DropdownMenuItem(
            onClick = {
                viewModel.navControllerMain.navigate(AppRoutes.MainRoutes.Settings.route)
                displayOptionsMenu = false
            },
            text = {
                Text(text = stringResource(id = R.string.settings))
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings)
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                viewModel.persona.showHistory()
                displayOptionsMenu = false
            },
            text = {
                Text(text = stringResource(id = R.string.bookmarks))
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bookmarks_24),
                    contentDescription = stringResource(id = R.string.bookmarks)
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                viewModel.persona.showList()
                displayOptionsMenu = false
            },
            text = {
                Text(text = stringResource(id = R.string.personas))
                Spacer(modifier = Modifier.width(0.25.adw))
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.list),
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                viewModel.chat.signOut {
                    viewModel.navController.navigate(AppRoutes.Landing.route)
                }
                displayOptionsMenu = false
            },
            text = {
                Text(text = stringResource(id = R.string.sign_out))
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = stringResource(id = R.string.sign_out)
                )
            }
        )
    }
}

