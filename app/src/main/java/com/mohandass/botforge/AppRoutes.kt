// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge

/**
 * Routes for the app
 *
 * AppRoutes:
 * All routes are defined here:
 *  [ -> represents a NavHost with the children as composable routes ]
 * MainActivity: ->
 *   - SplashUi
 *   - LandingUi
 *   - SignUpUi (deprecated)
 *   - SignInUi (deprecated)
 *   - OnBoardingUi
 *   - MainUi: ->
 *     - SettingsUi
 *     - ApiKeyUi
 *     - ApiUsageUi
 *     - ManageAccountUi
 *     - OpenSourceLibrariesUi
 *     - IconCreditsUi
 *     - AppInformationUi
 *     - PersonaUi: ->
 *       - ChatUi
 *       - HistoryUi
 *       - BrowseBotsUi
 *       - SharePersonaUi
 */
sealed class AppRoutes(val route: String) {
    object Splash : AppRoutes("splash_ui")
    object Landing : AppRoutes("landing_ui")
    object Main : AppRoutes("main_ui")
    object SignUp : AppRoutes("sign_up_ui")
    object SignIn : AppRoutes("sign_in_ui")
    object OnBoarding : AppRoutes("on_boarding_ui")

    sealed class MainRoutes(route: String) : AppRoutes(route) {
        sealed class PersonaRoutes(route: String) : MainRoutes(route) {
            object Chat : PersonaRoutes("chat_persona")
            object History : PersonaRoutes("history_persona")
            object Marketplace : PersonaRoutes("share_persona")
            object Share : PersonaRoutes("create_persona")
        }

        object Settings : MainRoutes("main_settings")
        object ApiKeySettings : MainRoutes("main_settings_api_key")
        object ApiUsageSettings : MainRoutes("main_settings_api_usage")
        object ManageAccountSettings : MainRoutes("main_settings_manage_account")
        object OpenSourceLicenses : MainRoutes("main_settings_open_source_licenses")
        object IconCredits : MainRoutes("main_settings_icon_credits")
        object AppInformation : MainRoutes("main_settings_app_information")
    }
}
