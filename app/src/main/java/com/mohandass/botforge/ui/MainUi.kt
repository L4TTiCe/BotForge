package com.mohandass.botforge.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.ui.components.AddAvatar
import com.mohandass.botforge.ui.components.RoundedIconFromString
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.ui.viewmodels.AppViewModel
import com.slaviboy.composeunits.dh
import com.slaviboy.composeunits.dw
import com.mohandass.botforge.R.drawable as AppDrawable
import com.mohandass.botforge.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi(appState: AppState?) {
    Scaffold(
        topBar = {
            TopBar(appState = appState)
        },
        content = {
            Column(
                modifier = Modifier.padding(it)
            ) {
                AvatarsBar()

                Spacer(modifier = Modifier.height(0.01.dh))

                NavHost(
                    navController = rememberNavController(),
                    startDestination = AppRoutes.MainRoutes.Default.route
                ) {
                    composable(AppRoutes.MainRoutes.Default.route) {
                        DefaultMainUi()
                    }
                }
            }
        }
    )
}

@Composable
fun DefaultMainUi() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Default Main UI")
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, viewModel: AppViewModel = hiltViewModel(), appState: AppState?) {
    var displayOptionsMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = AppDrawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = modifier
                .size(0.25.dw)
                .padding(10.dp)
        )

        Text(
            // Apply H3 style
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(AppText.app_name),
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { displayOptionsMenu = true }) {
            Icon(
                painter = painterResource(id = AppDrawable.vert_more),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(0.02.dw))

        DropdownMenu(
            expanded = displayOptionsMenu,
            onDismissRequest = { displayOptionsMenu = false },
            offset =  DpOffset(0.8.dw, (-0.01).dh)
        ) {
            DropdownMenuItem(onClick = { viewModel.signOut {
                appState?.navigateTo(AppRoutes.Landing.route)
            } }, text = { Text(text = "Sign Out") })
        }

    }
}

@Composable
fun AvatarsBar(modifier: Modifier = Modifier) {
    val avatars = remember { listOf("A", "Bc", "De.", "❤️", "\uD83D\uDD25", "F", "\uD83D\uDC80", "H", "I", "J") }

    LazyRow(modifier = modifier) {
        item {
            AddAvatar(
                modifier = Modifier
                    .size(90.dp)
                    .padding(6.dp)
            )
            Box(
                modifier = Modifier
                    .height(90.dp) // Matches the height of other elements in the row
                    .width(18.dp),
                contentAlignment = Alignment.Center // Centers its content vertically and horizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .height(64.dp) // Specifies the height of the spacer inside the box
                        .width(2.dp)
                        .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                )
            }
        }
        items(avatars.size) { index ->
//            RoundedIcon(
//                image = painterResource(id = R.drawable.logo),
//                modifier = Modifier
//                    .size(90.dp)
//                    .padding(6.dp)
//            )
            RoundedIconFromString(text = avatars[index], modifier = Modifier
                .size(90.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainUiPreview() {
    BotForgeTheme {
        MainUi(null)
    }
}
