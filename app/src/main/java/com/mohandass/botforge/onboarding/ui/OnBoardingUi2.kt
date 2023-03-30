package com.mohandass.botforge.onboarding.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun OnBoardingUi2(
    onNext: () -> Unit
) {
    val leftColor = colorResource(id = R.color.teal)
    val rightColor = colorResource(id = R.color.orange)
    val backgroundColor = colorResource(id = R.color.logoBorder)

    Box(
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            drawRect(
                color = leftColor,
                topLeft = Offset(0f, 0f),
                size = Size(width / 2f, height)
            )
            drawRect(
                color = rightColor,
                topLeft = Offset(width / 2f, 0f),
                size = Size(width / 2f, height)
            )
            drawLine(
                color = backgroundColor,
                strokeWidth = 60f,
                start = Offset(width / 2f, 0f),
                end = Offset(width / 2f, height)
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
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.6f),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = backgroundColor,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = onNext) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                            contentDescription = stringResource(id = R.string.next_cd),
                            tint = Color.White.copy(alpha = 0.8f),
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.9f),
                        text = stringResource(id = R.string.on_boarding_title_1),
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(horizontal = 40.dp)
                            .padding(20.dp),
                        text = stringResource(id = R.string.on_boarding_message_1),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

        }
    }
}

@Preview
@Composable
fun OnBoardingUi2Preview() {
    OnBoardingUi2 {}
}