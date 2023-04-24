// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services.implementation

import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.settings.model.PreferredTheme

class DisabledAnalytics : Analytics {
    override fun logAppOpened() {
        // Do nothing
    }

    override fun logLinkedWithGoogle() {
        // Do nothing
    }

    override fun logNameChanged() {
        // Do nothing
    }

    override fun logPreferredTheme(theme: PreferredTheme) {
        // Do nothing
    }

    override fun logIsMinimalHeaderEnabled(isEnabled: Boolean) {
        // Do nothing
    }

    override fun logIsDynamicColorEnabled(isEnabled: Boolean) {
        // Do nothing
    }

    override fun logIsShakeToClearEnabled(isEnabled: Boolean) {
        // Do nothing
    }

    override fun logShakeToClearSensitivity(sensitivity: Float) {
        // Do nothing
    }

    override fun logIsAnalyticsEnabled(isEnabled: Boolean) {
        // Do nothing
    }

    override fun logIsUgcEnabled(isEnabled: Boolean) {
        // Do nothing
    }

    override fun logIsImageGenerationEnabled(isEnabled: Boolean) {
        // Do nothing
    }

    override fun logIsAutoGenerateChatTitleEnabled(isEnabled: Boolean) {
        // Do nothing
    }

    override fun logCommunitySyncWithRemote(timestamp: Long) {
        // Do nothing
    }

    override fun logBotSharedWithCommunity() {
        // Do nothing
    }

    override fun logCommunityBotDownloaded() {
        // Do nothing
    }

    override fun logJsonExported() {
        // Do nothing
    }

    override fun logPdfExported() {
        // Do nothing
    }
}
