<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

### Common

- `common`: Contains the common code used by the application.
    - UI:
        - `ShakeDetector`: To detect shakes on the device, using Accelerometer.
        - `SplashUi`: Splash Screen with Animation, shown when the application is launched.
        - `MainUi`: Composable that is a part of MainActivity with NavHost which contains:
            - `SettingsUi` (from `settings` package)
            - `ApiKeyUi` (from `settings` package)
            - `ApiUsageUi` (from `settings` package)
            - `ManageAccountUi` (from `settings` package)
            - `OpenSourceLibrariesUi` (from `settings` package)
            - `IconCreditsUi` (from `settings` package)
            - `AppInformationUi` (from `settings` package)
            - `PersonaUi` (from `chat` package)
        - `Theme`: Contains the theme of the application.
            - Uses Material Design 3, with user configurable Light/Dark Mode and Dynamic Colors.
    - services:
        - `Logger`: A simple logger service. Implementations: `AndroidLogger`
          , `FirebaseCrashylyticsLogger`
        - `LocalDatabase`: Room Database for the application. Contains all entities and DAOs.
        - `SnackbarManager` and `SnackbarLauncher`: To handle Snackbars.
            - Supports Actions, DismissLabels with Callbacks.
        - `Utils`: Contains utility functions.
            - parseStackTraceForErrorMessage: To parse stack traces for error messages from OpenAI
              API.
            - randomEmojiUnicode: To get a random emoji unicode.
            - containsMarkdown: To check if a string contains markdown.
