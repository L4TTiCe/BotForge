package com.mohandass.botforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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