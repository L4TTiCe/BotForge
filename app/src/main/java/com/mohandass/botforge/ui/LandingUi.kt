package com.mohandass.botforge.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohandass.botforge.MainActivity
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.slaviboy.composeunits.dh

@Composable
fun Landing(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.size(0.2.dh))

        Text(
            text = "BotForge",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 24.sp,
        )

        Spacer(modifier = Modifier.size(0.05.dh))

        Image(
            painter = painterResource(id = com.mohandass.botforge.R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.size(0.15.dh))

        Button(
            onClick = {
                // Navigate to Main Activity
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Skip Login")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LandingUiPreview() {
    BotForgeTheme {
        Landing()
    }
}
