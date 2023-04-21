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
    fun logIsMinimalHeaderEnabled(isEnabled: Boolean)
    fun logIsDynamicColorEnabled(isEnabled: Boolean)
    fun logIsShakeToClearEnabled(isEnabled: Boolean)
    fun logShakeToClearSensitivity(sensitivity: Float)

    fun logIsAnalyticsEnabled(isEnabled: Boolean)
    fun logIsUgcEnabled(isEnabled: Boolean)
    fun logIsAutoGenerateChatTitleEnabled(isEnabled: Boolean)
    fun logCommunitySyncWithRemote(timestamp: Long)
    fun logBotSharedWithCommunity()
    fun logCommunityBotDownloaded()

    fun logJsonExported()
    fun logPdfExported()

    companion object {
        const val LINKED_WITH_GOOGLE = "linked_with_google"
        const val NAME_CHANGED = "name_changed"

        const val PREFERRED_THEME_AUTO = "preferred_theme_auto"
        const val PREFERRED_THEME_LIGHT = "preferred_theme_light"
        const val PREFERRED_THEME_DARK = "preferred_theme_dark"
        const val MINIMAL_HEADER_ENABLED = "minimal_header_enabled"
        const val MINIMAL_HEADER_DISABLED = "minimal_header_disabled"
        const val DYNAMIC_COLOR_ENABLED = "dynamic_color_enabled"
        const val DYNAMIC_COLOR_DISABLED = "dynamic_color_disabled"
        const val SHAKE_TO_CLEAR_ENABLED = "shake_to_clear_enabled"
        const val SHAKE_TO_CLEAR_DISABLED = "shake_to_clear_disabled"
        const val SHAKE_TO_CLEAR_SENSITIVITY = "shake_to_clear_sensitivity"

        const val ANALYTICS_ENABLED = "analytics_enabled"
        const val ANALYTICS_DISABLED = "analytics_disabled"
        const val UGC_ENABLED = "ugc_enabled"
        const val UGC_DISABLED = "ugc_disabled"
        const val AUTO_GENERATE_CHAT_TITLE_ENABLED = "auto_generate_chat_title_enabled"
        const val AUTO_GENERATE_CHAT_TITLE_DISABLED = "auto_generate_chat_title_disabled"
        const val COMMUNITY_SYNC_WITH_REMOTE = "community_sync_with_remote"
        const val BOT_SHARED_WITH_COMMUNITY = "bot_shared_with_community"
        const val COMMUNITY_BOT_DOWNLOADED = "community_bot_downloaded"

        const val JSON_EXPORTED = "json_exported"
        const val PDF_EXPORTED = "pdf_exported"
    }
}
