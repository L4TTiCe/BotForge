package com.mohandass.botforge

sealed class AppRoutes(val route: String) {
    object Splash: AppRoutes("splash_ui")
    object Landing: AppRoutes("landing_ui")
    object Main: AppRoutes("main_ui")
    object SignUp: AppRoutes("sign_up_ui")
    object SignIn: AppRoutes("sign_in_ui")

    sealed class MainRoutes(route: String): AppRoutes(route) {
        sealed class PersonaRoutes(route: String): MainRoutes(route) {
            object Chat: PersonaRoutes("chat_persona")
            object History: PersonaRoutes("history_persona")
        }
        object Settings: MainRoutes("main_settings")
        object ApiKeySettings: MainRoutes("main_settings_api_key")
        object ApiUsageSettings: MainRoutes("main_settings_api_usage")
        object ManageAccountSettings: MainRoutes("main_settings_manage_account")
    }
}
