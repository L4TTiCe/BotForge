package com.mohandass.botforge.navigation

sealed class Screen(val route: String) {
    object Splash: Screen("splash_ui")
    object Landing: Screen("landing_ui")
    object Main: Screen("main_ui")
}
