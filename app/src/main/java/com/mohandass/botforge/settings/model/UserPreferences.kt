package com.mohandass.botforge.settings.model

data class UserPreferences(
    val preferredTheme: PreferredTheme,
    val useDynamicColors: Boolean,
    val lastSuccessfulSync: Long,
    val enableUserGeneratedContent: Boolean,
    val enableShakeToClear: Boolean,
    val shakeToClearSensitivity: Float = 0f,
) {
    override fun toString(): String {
        return "UserPreferences(" +
                "preferredTheme=$preferredTheme, " +
                "useDynamicColors=$useDynamicColors, " +
                "lastSuccessfulSync=$lastSuccessfulSync" +
                "enableUserGeneratedContent=$enableUserGeneratedContent" +
                "enableShakeToClear=$enableShakeToClear" +
                "shakeToClearSensitivity=$shakeToClearSensitivity" +
                ")"
    }
}