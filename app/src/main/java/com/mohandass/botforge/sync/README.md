<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

### Sync

- `sync`: Handles syncing, and sharing Bots.
    - UI:
        - `BrowseBotsUi`: Displays a list of Community Bots stored locally in Room Database. Allows user to search, and sync with remote database. Displays each Bot using a `BotCard`
        - `SharePersonaUi`: Allows users to share their Persona with other users.
        - Components:
            - `BotCard`: Displays a Bot with its name, description, and a button to Add the Bot.
            - `BotDetailDialog`: Displays the details of a Bot. Supports UpVoting, DownVoting, and Reporting the Bot.
    - Dependencies:
        - `BotService`: Handles syncing with remote database, and storing Bots locally. Depends on:
            - `FirebaseRealtimeDatabase`: Remote Database to store Community Bots.
            - `FirebaseFirestore`: Remote Database to store UpVotes, DownVotes and Reports.

#### Syncing

BotService handles syncing with remote database, and storing Bots locally. It stores the last
successful sync timestamp in `UserPreferences` in `PreferencesDataStore`. The timestamp is used
to fetch bots that have been added or updated since the last sync. The timestamp is then updated
to the current time. This allows incremental syncing.

#### Searching

Every time a Bot is added or updated, it is stored locally in Room Database as `BotE` (Entity). and
`BotFts` (Full Text Search). The `BotFts` is used to search for bots. The search is done using
Room's MATCH query.

#### Content Moderation

Bots marked for deletion are stored in a different collection, and are indexed incrementally. 
The last processed index is stored in `UserPreferences` in `PreferencesDataStore`. The index is 
used to fetch the next batch of bots to be moderated. The index is then updated to the highest 
index processed.