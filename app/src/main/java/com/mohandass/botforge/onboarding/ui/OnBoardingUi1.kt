// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.onboarding.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohandass.botforge.R

@Composable
fun OnBoardingUi1() {
    val leftColor = colorResource(id = R.color.teal)
    val rightColor = colorResource(id = R.color.orange)
    val backgroundColor = colorResource(id = R.color.logoBorder)

    val systemUiController = rememberSystemUiController()

    DisposableEffect(
        systemUiController,
    ) {
        systemUiController.setSystemBarsColor(
            color = backgroundColor
        )
        onDispose { }
    }

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
                    .fillMaxHeight(0.8f),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.logo_cd)
            )

        }
    }
}

@Preview
@Composable
fun OnBoardingUi1Preview() {
    MaterialTheme {
        OnBoardingUi1()
    }
}
