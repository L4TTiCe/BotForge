package com.mohandass.botforge.settings.model

data class UserPreferences(
    val preferredTheme: PreferredTheme,
    val useDynamicColors: Boolean
) {
    override fun toString(): String {
        return "UserPreferences(preferredTheme=$preferredTheme, useDynamicColors=$useDynamicColors)"
    }
}