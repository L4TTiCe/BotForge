package com.mohandass.botforge.model.preferences

data class UserPreferences(
    val preferredTheme: PreferredTheme,
    val useDynamicColors: Boolean
) {
    override fun toString(): String {
        return "UserPreferences(preferredTheme=$preferredTheme, useDynamicColors=$useDynamicColors)"
    }
}