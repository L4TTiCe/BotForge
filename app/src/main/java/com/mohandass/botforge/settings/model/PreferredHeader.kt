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
