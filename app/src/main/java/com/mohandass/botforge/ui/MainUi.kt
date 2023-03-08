package com.mohandass.botforge.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.components.AddAvatar
import com.mohandass.botforge.ui.components.RoundedIconFromString
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.slaviboy.composeunits.dw

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi(appState: AppState?) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .background(MaterialTheme.colorScheme.background),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        TopBar()
//    }

    Scaffold(
//        topBar = {
//            MediumTopAppBar(
//                navigationIcon = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            painter = painterResource(id = com.mohandass.botforge.R.drawable.logo),
//                            contentDescription = null,
//                            tint = Color.Unspecified
//                        )
//                    }
//                },
//                title = {
//                    Text(
//                        text = stringResource(com.mohandass.botforge.R.string.app_name),
//                    )
//                },
//            )
//        },
        topBar = {
            TopBar()
        },
        content = {
            AvatarsBar(paddingValues = it)
        }
    )
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
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

    }
}

@Composable
fun AvatarsBar(paddingValues: PaddingValues) {
    val avatars = remember { listOf("A", "Bc", "De.", "❤️", "\uD83D\uDD25", "F", "\uD83D\uDC80", "H", "I", "J") }

    LazyRow(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
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
