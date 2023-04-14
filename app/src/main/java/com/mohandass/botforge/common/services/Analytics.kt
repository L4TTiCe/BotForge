// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

import com.mohandass.botforge.settings.model.PreferredTheme

interface Analytics {
    fun logAppOpened()

    fun logLinkedWithGoogle()
    fun logNameChanged()

    fun logPreferredTheme(theme: PreferredTheme)
    fun logIsMinimalHeaderEnabled(boolean: Boolean)
    fun logIsDynamicColorEnabled(boolean: Boolean)
    fun logIsShakeToClearEnabled(boolean: Boolean)
    fun logShakeToClearSensitivity(sensitivity: Float)

    fun logIsAnalyticsEnabled(boolean: Boolean)
    fun logIsUgcEnabled(boolean: Boolean)
    fun logCommunitySyncWithRemote(timestamp: Long)
    fun logBotSharedWithCommunity()
    fun logCommunityBotDownloaded()

    fun logJsonExported()
    fun logPdfExported()

    companion object {
        const val LINKED_WITH_GOOGLE = "linked_with_google"
        const val NAME_CHANGED = "name_changed"

        const val PREFERRED_THEME = "preferred_theme"
        const val IS_MINIMAL_HEADER_ENABLED = "is_minimal_header_enabled"
        const val IS_DYNAMIC_COLOR_ENABLED = "is_dynamic_color_enabled"
        const val IS_SHAKE_TO_CLEAR_ENABLED = "is_shake_to_clear_enabled"
        const val SHAKE_TO_CLEAR_SENSITIVITY = "shake_to_clear_sensitivity"

        const val IS_ANALYTICS_ENABLED = "is_analytics_enabled"
        const val IS_UGC_ENABLED = "is_ugc_enabled"
        const val COMMUNITY_SYNC_WITH_REMOTE = "community_sync_with_remote"
        const val BOT_SHARED_WITH_COMMUNITY = "bot_shared_with_community"
        const val COMMUNITY_BOT_DOWNLOADED = "community_bot_downloaded"

        const val JSON_EXPORTED = "json_exported"
        const val PDF_EXPORTED = "pdf_exported"
    }
}
