package com.mohandass.botforge

sealed class Screen(val route: String) {
    object Splash: Screen("splash_ui")
    object Landing: Screen("landing_ui")
    object Main: Screen("main_ui")
    object SignUp: Screen("sign_up_ui")
    object SignIn: Screen("sign_in_ui")
}
