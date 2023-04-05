<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

### Chat

- `chat`: Contains the chat related code. (Main)
    - Includes the bulk of the code.
    - UI:
        - `PersonaUi`: Parent composable for the chat screen. Contains NavHost which contains:
            - `ChatUi`: with Editing Persona and `MessageList`.
            - `HistoryUi`: composable to show saved Chats, with each Chat as a `ChatCard`
            - `ExperimentalPersonaUi`: Another implementation of `ChatUi` with Bottom Sheet. (Not used)
        - Components:
            - Chat Components:
                - `MessageList`: List of `MessageEntry` with `LazyColumn`.
                - `MessageEntry`: A single message entry in the `MessageList`. Contains `MessageEntryField`, `MessageEntrySideIcons` and `MessageMetadata`.
                - `TypingIndicator`: A typing indicator for the chat. Composed with multiple `TypingIndicatorDot`.
                - `MarkdownCard`: A card to show markdown content in the chat.
                - `MarkdownInfoCard`:A banner to indicate that the message is markdown.
            - Dialogs
                - Dialogs relevant to Deleting / Saving Personas and Chats.
            - Header
                - `AvatarBar`: The bar at the top of the screen that shows all the Personas the user has saved. Uses `TintedIconButton` for Adding new Persona and Community Icons, while using `RoundedIconFromString` and `RoundedIconFromStringAnimated` for Peronas.
                - Other components used in the header.
            - Icons
                - `RoundedIconFromString`: A rounded icon with a Border
                - `RoundedIconFromStringAnimated`: A rounded icon with an animated Border
                - `TintedIconButton`: Based on `RoundedIconFromString`, but uses an Icon, and can be animated.
            - `ChatCard`: A card to show a saved Chat.
    - Dependencies:
        - `PersonaService`: Save and Read Personas to local Room Database.
        - `ChatService`: Save and Read Chats to local Room Database.
        - `OpenAiService`: Remote API to get responses from OpenAI.