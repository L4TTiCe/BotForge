package com.mohandass.botforge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.viewmodels.AppViewModel
import com.slaviboy.composeunits.dh
import com.slaviboy.composeunits.dw

@Composable
fun Header(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    appState: AppState? = null
) {
    var displayOptionsMenu by remember { mutableStateOf(false) }

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
                .size(0.25.dw)
                .padding(10.dp)
        )

        Text(
            // Apply H3 style
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(R.string.app_name),
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { displayOptionsMenu = true }) {
            Icon(
                painter = painterResource(id = R.drawable.vert_more),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(0.02.dw))

        DropdownMenu(
            expanded = displayOptionsMenu,
            onDismissRequest = { displayOptionsMenu = false },
            offset =  DpOffset(0.8.dw, (-0.01).dh),
        ) {
            DropdownMenuItem(
                onClick = {
                    viewModel.signOut {
                        appState?.navigateTo(AppRoutes.Landing.route)
                    }
                },
                text = {
                    Text(text = "Sign Out")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null)
                }
            )
        }

    }
}
