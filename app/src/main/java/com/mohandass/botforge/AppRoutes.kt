package com.mohandass.botforge

sealed class AppRoutes(val route: String) {
    object Splash: AppRoutes("splash_ui")
    object Landing: AppRoutes("landing_ui")
    object Main: AppRoutes("main_ui")
    object SignUp: AppRoutes("sign_up_ui")
    object SignIn: AppRoutes("sign_in_ui")

    sealed class MainRoutes(route: String): AppRoutes(route) {
        object Default: MainRoutes("main_default")
    }
}
