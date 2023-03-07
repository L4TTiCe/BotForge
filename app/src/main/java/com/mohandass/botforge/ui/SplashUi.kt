package com.mohandass.botforge.ui

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mohandass.botforge.R
import com.mohandass.botforge.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashUi(navController: NavController) {
    val scale =  remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = null) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }),
        )

        delay(1000)
        navController.navigate(Screen.Landing.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo_cd) )
    }
}