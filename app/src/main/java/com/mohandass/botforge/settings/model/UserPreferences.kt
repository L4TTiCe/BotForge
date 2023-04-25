// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.model

/**
 * User preferences for the app
 */
data class UserPreferences(
    val preferredTheme: PreferredTheme,
    val useDynamicColors: Boolean,
    val preferredHeader: PreferredHeader,
    val lastSuccessfulSync: Long,
    val enableUserGeneratedContent: Boolean,
    val enableShakeToClear: Boolean,
    val shakeToClearSensitivity: Float = 0f,
    val lastModerationIndexProcessed: Int,
    val autoGenerateChatTitle: Boolean,
    val enableImageGeneration: Boolean,
) {
    override fun toString(): String {
        return "UserPreferences(" +
                "preferredTheme=$preferredTheme, " +
                "useDynamicColors=$useDynamicColors, " +
                "preferredHeader=$preferredHeader, " +
                "lastSuccessfulSync=$lastSuccessfulSync" +
                "enableUserGeneratedContent=$enableUserGeneratedContent" +
                "enableShakeToClear=$enableShakeToClear" +
                "shakeToClearSensitivity=$shakeToClearSensitivity" +
                "lastModerationIndex=$lastModerationIndexProcessed" +
                "autoGenerateChatTitle=$autoGenerateChatTitle" +
                "enableImageGeneration=$enableImageGeneration" +
                ")"
    }
}
