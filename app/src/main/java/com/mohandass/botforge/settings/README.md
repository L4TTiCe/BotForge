<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

### Settings

- `settings`: Contains the settings related code.
    - UI:
        - `SettingsUi`: Composable which leads to the following screens:
            - `ApiKeyUi`: to manage API Key. Links to OpenAI API Key Page.
            - `ApiUsageUi`: displays estimated API Usage, links to OpenAI API Usage Page, allows
              user to reset API Usage.
            - `ApperanceSettings`: Allows user to enable Dark Mode and Dynamic Colors.
            - `AppInformationUi`: Shows App Information.
            - `IconCreditsUi`: To attribute and show Icon used in the application.
            - `OpenSourceLibrariesUi`: To attribute Open Source Libraries.
    - Dependencies:
        - `SharedPreferencesService`: Stores ApiKey, OnBoardingCompleted and usageTokens.
        - `PreferencesDataStore`: Stores `UserPreferences` which holds
            - preferredTheme: Light, Dark or System Default
            - dynamicColors: Boolean
            - lastSuccessfulSync: Long (Timestamp) (Community Bots Sync with Realtime Database)
            - enableUserGeneratedContent: Boolean
            - enableShakeToClear: Boolean
            - shakeToClearSensitivity: Float
            - lastModerationIndexProcessed: Int (Content Moderation)
