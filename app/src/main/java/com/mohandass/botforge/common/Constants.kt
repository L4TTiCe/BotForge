// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common

class Constants {
    companion object {
        const val DEFAULT_CHAT_MODEL = "gpt-3.5-turbo"

        const val DEFAULT_API_TIMEOUT = 60
        const val MAX_API_TIMEOUT = 300
        const val MIN_API_TIMEOUT = 15

        const val ANIMATION_DURATION = 400
        const val ANIMATION_OFFSET = 400

        const val ICONS_SIZE = 90
        const val FOLDABLE_THRESHOLD = 600

        const val MAX_IMAGE_GENERATION_COUNT = 5

        const val MAX_SENSITIVITY_THRESHOLD = 5f
    }
}
