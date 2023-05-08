<!--
SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe) <dheshan@mohandass.com>
SPDX-License-Identifier: MIT
-->

### Image

- `image`: Contains Image Generation related code
  - UI
    - `ImageUi`: Parent composable for the Image Generation screen.
    - Components
      - `GeneratedImageHistoryItem`: A single item in the Generated Image History.
  - Dependencies
    - `ImageGenerationService`: Reads and Writes Generated Images to local Room Database.
    - `OpenAiService`: Remote API to get responses from OpenAI.

