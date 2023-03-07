package com.mohandass.botforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.navigation.Screen
import com.mohandass.botforge.ui.LandingUi
import com.mohandass.botforge.ui.MainUi
import com.mohandass.botforge.ui.SplashUi
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.slaviboy.composeunits.initSize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSize()

        setContent {
            BotForgeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashUi(navController = navController)
        }
        composable(Screen.Landing.route) {
            LandingUi(navController = navController)
        }
        composable(Screen.Main.route) {
            MainUi(navController = navController)
        }
    }
}