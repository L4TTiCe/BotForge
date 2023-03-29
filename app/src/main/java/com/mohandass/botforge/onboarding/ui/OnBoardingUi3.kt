package com.mohandass.botforge.onboarding.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingUi3(
    initialApiKey: String = "",
    saveApiKey: (String) -> Unit
) {
    val backgroundColor = colorResource(id = R.color.logoBorder)
    var apiKey by remember { mutableStateOf(initialApiKey) }

    val context = LocalContext.current

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
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    text = "Before we begin...",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(horizontal = 40.dp)
                        .padding(20.dp),
                    text = "You will need an OpenAI API key to use this app.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = {
                        apiKey = it
                        saveApiKey(it)
                    },
                    label = {
                        Text(
                            text = resources().getString(R.string.api_key),
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    },
                    placeholder = {
                        Text(
                            text = resources().getString(R.string.api_key_hint),
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 2.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            saveApiKey(apiKey)
                        }
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                GetKey(
                    title = resources().getString(R.string.open_ai_api_website),
                    description = "Tap to visit the OpenAI's API page to get your API key.",
                    painter = painterResource(id = R.drawable.baseline_key_24),
                ) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(context.getString(R.string.api_key_link))
                    }
                    context.startActivity(intent)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}

@Composable
fun GetKey(title: String, description: String, painter: Painter, onClick: () -> Unit) {
    val backgroundColor = colorResource(id = R.color.logoBorder)

    Surface(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(8.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 14.dp, end = 16.dp)
                    .size(32.dp),
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
        }
    }
}

@Preview
@Composable
fun OnBoardingUi3Preview() {
    OnBoardingUi3(
        saveApiKey = {}
    )
}
