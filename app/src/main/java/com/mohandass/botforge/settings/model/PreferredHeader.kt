// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.model

enum class PreferredHeader {
    DEFAULT_HEADER,
    MINIMAL_HEADER;

    override fun toString(): String {
        return when (this) {
            DEFAULT_HEADER -> "Default"
            MINIMAL_HEADER -> "Minimal"
        }
    }
}
