// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
fun MainHeader(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel
) {
    val title by viewModel.topBar.title
    val overrideMenu by viewModel.topBar.overrideMenu
    val menu by viewModel.topBar.menu

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo_cd),
            contentScale = ContentScale.Inside,
            modifier = modifier
                .size(if (Constants.ICONS_SIZE.dp < 0.20.adw) Constants.ICONS_SIZE.dp else 0.20.adw)
                .padding(10.dp)
        )

        Text(
            // Apply H3 style
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(title),
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        if (overrideMenu) {

            menu()

            Spacer(modifier = Modifier.width(0.02.adw))
        } else {
            DefaultDropdownMenu(modifier, viewModel)
        }

    }
}

@Composable
fun DefaultDropdownMenu(modifier: Modifier = Modifier, viewModel: AppViewModel) {
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
            modifier = modifier
                .size(0.05.adh)
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
                viewModel.signOut {
                    viewModel.navController.navigate(AppRoutes.Landing.route)
                }
                displayOptionsMenu = false
            },
            text = {
                Text(text = "Sign Out")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = stringResource(id = R.string.sign_out)
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                openDeleteConfirmationDialog.value = true
            },
            text = {
                Text(text = "Delete All Personas")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_all_persona)
                )
            }
        )
    }
}
