<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

### Auth 

- `auth`: Contains the authentication related code.
    - UI:
        - `LandingUi`: with Google Sign In and Anonymous Sign In.
        - ~~`SignInUi`: Email and Password Login~~ (No longer used)
        - ~~`SignUpUi`: Email and Password SignUp~~ (No longer used)
    - Dependencies:
        - `FirebaseAuth`
        - `AccountService`
            - Handles generateUsername: Which generates a username randomly from a list of adjectives and nouns (found at app/src/main/res/values/name_generation.xml).
    
#### Authentication

Authentication is handled by Firebase Authentication. The user can sign in using Google Sign In or 
Anonymous Sign In. ~~The user can also sign up using Email and Password.~~
