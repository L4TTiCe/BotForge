// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services.implementation

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.settings.model.PreferredTheme

class FirebaseAnalyticsImpl : Analytics {
    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun logAppOpened() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) { }
    }

    override fun logLinkedWithGoogle() {
        firebaseAnalytics.logEvent(Analytics.LINKED_WITH_GOOGLE) { }
    }

    override fun logNameChanged() {
        firebaseAnalytics.logEvent(Analytics.NAME_CHANGED) { }
    }

    override fun logIsAnalyticsEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            firebaseAnalytics.logEvent(Analytics.ANALYTICS_ENABLED) { }
        } else {
            firebaseAnalytics.logEvent(Analytics.ANALYTICS_DISABLED) { }
        }
    }

    override fun logPreferredTheme(theme: PreferredTheme) {
        when (theme) {
            PreferredTheme.AUTO -> firebaseAnalytics.logEvent(Analytics.PREFERRED_THEME_AUTO) { }
            PreferredTheme.LIGHT -> firebaseAnalytics.logEvent(Analytics.PREFERRED_THEME_LIGHT) { }
            PreferredTheme.DARK -> firebaseAnalytics.logEvent(Analytics.PREFERRED_THEME_DARK) { }
        }
    }

    override fun logIsMinimalHeaderEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            firebaseAnalytics.logEvent(Analytics.MINIMAL_HEADER_ENABLED) { }
        } else {
            firebaseAnalytics.logEvent(Analytics.MINIMAL_HEADER_DISABLED) { }
        }
    }

    override fun logIsDynamicColorEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            firebaseAnalytics.logEvent(Analytics.DYNAMIC_COLOR_ENABLED) { }
        } else {
            firebaseAnalytics.logEvent(Analytics.DYNAMIC_COLOR_DISABLED) { }
        }
    }

    override fun logIsShakeToClearEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            firebaseAnalytics.logEvent(Analytics.SHAKE_TO_CLEAR_ENABLED) { }
        } else {
            firebaseAnalytics.logEvent(Analytics.SHAKE_TO_CLEAR_DISABLED) { }
        }
    }

    override fun logShakeToClearSensitivity(sensitivity: Float) {
        firebaseAnalytics.logEvent(Analytics.SHAKE_TO_CLEAR_SENSITIVITY) {
            param(Analytics.SHAKE_TO_CLEAR_SENSITIVITY, sensitivity.toDouble())
        }
    }

    override fun logIsUgcEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            firebaseAnalytics.logEvent(Analytics.UGC_ENABLED) { }
        } else {
            firebaseAnalytics.logEvent(Analytics.UGC_DISABLED) { }
        }
    }

    override fun logIsImageGenerationEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            firebaseAnalytics.logEvent(Analytics.IMAGE_GENERATION_ENABLED) { }
        } else {
            firebaseAnalytics.logEvent(Analytics.IMAGE_GENERATION_DISABLED) { }
        }
    }

    override fun logIsAutoGenerateChatTitleEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            firebaseAnalytics.logEvent(Analytics.AUTO_GENERATE_CHAT_TITLE_ENABLED) { }
        } else {
            firebaseAnalytics.logEvent(Analytics.AUTO_GENERATE_CHAT_TITLE_DISABLED) { }
        }
    }

    override fun logCommunitySyncWithRemote(timestamp: Long) {
        firebaseAnalytics.logEvent(Analytics.COMMUNITY_SYNC_WITH_REMOTE) {
            param(Analytics.COMMUNITY_SYNC_WITH_REMOTE, timestamp)
        }
    }

    override fun logBotSharedWithCommunity() {
        firebaseAnalytics.logEvent(Analytics.BOT_SHARED_WITH_COMMUNITY) { }
    }

    override fun logCommunityBotDownloaded() {
        firebaseAnalytics.logEvent(Analytics.COMMUNITY_BOT_DOWNLOADED) { }
    }

    override fun logImageGenerated() {
        firebaseAnalytics.logEvent(Analytics.IMAGE_GENERATED) { }
    }

    override fun logJsonExported() {
        firebaseAnalytics.logEvent(Analytics.JSON_EXPORTED) { }
    }

    override fun logPdfExported() {
        firebaseAnalytics.logEvent(Analytics.PDF_EXPORTED) { }
    }

    override fun logImageExported() {
        firebaseAnalytics.logEvent(Analytics.IMAGE_EXPORTED) { }
    }

    companion object {
        const val TAG = "FirebaseAnalyticsImpl"
    }
}
