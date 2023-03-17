package com.mohandass.botforge.model.preferences

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
