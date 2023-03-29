package com.mohandass.botforge.onboarding.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun OnBoardingUi4(
    initialUserGenerated: Boolean = true,
    saveUserGeneratedContent: (Boolean) -> Unit,
    onComplete: () -> Unit
) {
    val backgroundColor = colorResource(id = R.color.logoBorder)
    val enableUserGeneratedContent = remember { mutableStateOf(initialUserGenerated) }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            drawRect(
                color = backgroundColor,
                topLeft = Offset(0f, 0f),
                size = Size(width , height)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Image(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight(0.2f),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    text = "Finally,",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(horizontal = 40.dp)
                        .padding(20.dp),
                    text = "Do you want to enable user generated content?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                UserGeneratedContentSwitch(
                    title = "User Generated Content",
                    description = "Allow bots from the community to be displayed in the app",
                    icon = painterResource(id = R.drawable.baseline_color_lens_24),
                    switchState = enableUserGeneratedContent,
                    onCheckChange = {
                        enableUserGeneratedContent.value = it
                        saveUserGeneratedContent(it)
                    }
                )

                Spacer(modifier = Modifier.height(60.dp))

                Button(onClick = onComplete) {
                    Text(
                        text = "Get Started",
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}

@Composable
fun UserGeneratedContentSwitch(
    title: String,
    description: String,
    icon: Painter,
    switchState: MutableState<Boolean>,
    onCheckChange: (Boolean) -> Unit,
) {
    val backgroundColor = colorResource(id = R.color.logoBorder)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp, 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 14.dp, end = 16.dp)
                .size(26.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp)
        ) {
            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Switch(
            checked = switchState.value,
            onCheckedChange = { onCheckChange(it) },
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
    }
}

@Preview
@Composable
fun OnBoardingUi4Preview() {
    OnBoardingUi4(
        saveUserGeneratedContent = {  },
        onComplete = {  }
    )
}
