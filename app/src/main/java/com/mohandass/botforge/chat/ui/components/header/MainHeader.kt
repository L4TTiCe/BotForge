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
import com.slaviboy.composeunits.dh
import com.slaviboy.composeunits.dw

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
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = modifier
                .size(if (150.dp < 0.20.dw) 150.dp else 0.20.dw)
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

            Spacer(modifier = Modifier.width(0.02.dw))
        } else {
            DefaultDropdownMenu(modifier, viewModel)
        }

    }
}

@Composable
fun DefaultDropdownMenu( modifier: Modifier = Modifier, viewModel: AppViewModel) {
    var displayOptionsMenu by remember { mutableStateOf(false) }
    val openDeleteConfirmationDialog = remember { mutableStateOf(false) }

    IconButton(onClick = { displayOptionsMenu = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            modifier = modifier
                .size(0.05.dh)
        )
    }

    Spacer(modifier = Modifier.width(0.02.dw))

    DropdownMenu(
        expanded = displayOptionsMenu,
        onDismissRequest = { displayOptionsMenu = false },
        offset =  DpOffset(0.8.dw, (0.01).dh),
    ) {
        DropdownMenuItem(
            onClick = {
                viewModel.navigateTo(AppRoutes.MainRoutes.Settings.route)
                displayOptionsMenu = false
            },
            text = {
                Text(text = "Preferences")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
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
                    contentDescription = null
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
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                throw RuntimeException("Test Crash")
            },
            text = {
                Text(text = "TEST CRASH")
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bug_report_24),
                    contentDescription = null
                )
            }
        )

        if (openDeleteConfirmationDialog.value) {
            AlertDialog(
                onDismissRequest = { openDeleteConfirmationDialog.value = false },
                title = {
                    Text(
                        text = stringResource(id = R.string.delete_all_persona),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.delete_all_persona_message),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.persona.deleteAllPersonas()
                        viewModel.persona.clearSelection()
                        openDeleteConfirmationDialog.value = false
                    }) {
                        Text(
                            text = stringResource(id = R.string.delete_all),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        openDeleteConfirmationDialog.value = false
                    }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                textContentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}
