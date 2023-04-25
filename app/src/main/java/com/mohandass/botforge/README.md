<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

## Project Structure

The project is divided multiple packages to separate the different parts of the application.

Each package will follow the following structure:

- `model`: Contains the data models used by the application.
    - `dao`: Contains the data access objects used to access the data models.
        - `entities`: Contains the data models used by the data access objects.
- `service`: Contains the services used by the application.
    - `implemetation`: Contains the implementations of the services.
- `ui`: Contains the user interface of the application.
    - `components`: Contains the components used by the user interface. (mostly stateless)
    - Could be further categorised into features.
- `viewmodel`: Contains the view models used by the the package.
- PackageModule: Contains the module used to provide the dependencies of the package, using DI.

This project has the following packages:

- `auth`: Contains the authentication related code.
- `chat`: Contains the chat related code. (Main)
- `common`: Contains the common code used by the application.
- `image`: Contains Image Generation related code
- `onboarding`: OnBoarding Screens shown on first launch.
- `settings`: Contains the settings related code.
- `sync`: Handles syncing, and sharing Bots.

### Navigation

The following list shows the navigation graph of the application.

- `MainActivity` [Start Destination]
    - `SplashUi` (from `common` package)
    - `LandingUi` (from `auth` package)
    - `MainUi` (from `common` package) [Contains NavHost]
        - `SettingsUi` (from `settings` package)
        - `ApiKeyUi` (from `settings` package)
        - `ApiUsageUi` (from `settings` package)
        - `ManageAccountUi` (from `settings` package)
        - `OpenSourceLibrariesUi` (from `settings` package)
        - `IconCreditsUi` (from `settings` package)
        - `AppInformationUi` (from `settings` package)
        - `PersonaUi` (from `chat` package) [Contains NavHost]
            - `ChatUi` (from `chat` package)
            - `HistoryUi` (from `chat` package)
            - `PersonaListUi` (from `chat` package)
            - `ImageUi` (from `image` package)
            - `BrowseBotsUi` (from `sync` package)
            - `SharePersonaUi` (from `sync` package)
    - ~~`SignUpUi` (from `auth` package)~~ (Not Used)
    - ~~`SignInUi` (from `auth` package)~~ (Not Used)
    - `OnBoardingUi` (from `onboarding` package) [Contains VerticalPager]
        - `OnBoardingUi1Logo` (from `onboarding` package)
        - `OnBoardingUi2Welcome` (from `onboarding` package)
        - `OnBoardingUi3Api` (from `onboarding` package)
        - `OnBoardingUi4Ugc` (from `onboarding` package)

