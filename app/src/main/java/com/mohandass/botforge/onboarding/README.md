<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

### OnBoarding

- `onboarding`: OnBoarding Screens shown on first launch.
    - UI:
        - `OnBoardingUi`: Composable with Vertical Pager which contains:
            - `OnBoardingUi1Logo`
            - `OnBoardingUi2Welcome`
            - `OnBoardingUi3Api`: To get OpenAI API Key from User.
            - `OnBoardingUi4Ugc`: Ask User to allow or hide UGC.

#### OnBoarding Process

1. The above UI is shown on first launch, after the user has signed in.
2. The user is asked to enter the OpenAI API Key. (Optional)
3. The user is asked to allow or hide UGC. (Defaults to Allow)
4. The user is taken to the Main Screen.

