// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.model

/**
 * The preferred theme for the app
 */
enum class PreferredTheme {
    AUTO,
    LIGHT,
    DARK;

    override fun toString(): String {
        return when (this) {
            AUTO -> "Auto"
            LIGHT -> "Light"
            DARK -> "Dark"
        }
    }
}
