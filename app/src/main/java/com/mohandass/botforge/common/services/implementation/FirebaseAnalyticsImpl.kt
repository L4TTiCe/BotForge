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

class FirebaseAnalyticsImpl: Analytics {
    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun logAppOpened() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {  }
    }

    override fun logLinkedWithGoogle() {
        firebaseAnalytics.logEvent(Analytics.LINKED_WITH_GOOGLE) {  }
    }

    override fun logNameChanged() {
        firebaseAnalytics.logEvent(Analytics.NAME_CHANGED) {  }
    }

    override fun logIsAnalyticsEnabled(boolean: Boolean) {
        firebaseAnalytics.logEvent(Analytics.IS_ANALYTICS_ENABLED) {
            param(Analytics.IS_ANALYTICS_ENABLED, boolean.toString())
        }
    }

    override fun logPreferredTheme(theme: PreferredTheme) {
        firebaseAnalytics.logEvent(Analytics.PREFERRED_THEME) {
            param(Analytics.PREFERRED_THEME, theme.name)
        }
    }

    override fun logIsMinimalHeaderEnabled(boolean: Boolean) {
        firebaseAnalytics.logEvent(Analytics.IS_MINIMAL_HEADER_ENABLED) {
            param(Analytics.IS_MINIMAL_HEADER_ENABLED, boolean.toString())
        }
    }

    override fun logIsDynamicColorEnabled(boolean: Boolean) {
        firebaseAnalytics.logEvent(Analytics.IS_DYNAMIC_COLOR_ENABLED) {
            param(Analytics.IS_DYNAMIC_COLOR_ENABLED, boolean.toString())
        }
    }

    override fun logIsShakeToClearEnabled(boolean: Boolean) {
        firebaseAnalytics.logEvent(Analytics.IS_SHAKE_TO_CLEAR_ENABLED) {
            param(Analytics.IS_SHAKE_TO_CLEAR_ENABLED, boolean.toString())
        }
    }

    override fun logShakeToClearSensitivity(sensitivity: Float) {
        firebaseAnalytics.logEvent(Analytics.SHAKE_TO_CLEAR_SENSITIVITY) {
            param(Analytics.SHAKE_TO_CLEAR_SENSITIVITY, sensitivity.toDouble())
        }
    }

    override fun logIsUgcEnabled(boolean: Boolean) {
        firebaseAnalytics.logEvent(Analytics.IS_UGC_ENABLED) {
            param(Analytics.IS_UGC_ENABLED, boolean.toString())
        }
    }

    override fun logIsAutoGenerateChatTitleEnabled(boolean: Boolean) {
        firebaseAnalytics.logEvent(Analytics.IS_AUTO_GENERATE_CHAT_TITLE_ENABLED) {
            param(Analytics.IS_AUTO_GENERATE_CHAT_TITLE_ENABLED, boolean.toString())
        }
    }

    override fun logCommunitySyncWithRemote(timestamp: Long) {
        firebaseAnalytics.logEvent(Analytics.COMMUNITY_SYNC_WITH_REMOTE) {
            param(Analytics.COMMUNITY_SYNC_WITH_REMOTE, timestamp)
        }
    }

    override fun logBotSharedWithCommunity() {
        firebaseAnalytics.logEvent(Analytics.BOT_SHARED_WITH_COMMUNITY) {  }
    }

    override fun logCommunityBotDownloaded() {
        firebaseAnalytics.logEvent(Analytics.COMMUNITY_BOT_DOWNLOADED) {  }
    }

    override fun logJsonExported() {
        firebaseAnalytics.logEvent(Analytics.JSON_EXPORTED) {  }
    }

    override fun logPdfExported() {
        firebaseAnalytics.logEvent(Analytics.PDF_EXPORTED) {  }
    }

    companion object {
        const val TAG = "FirebaseAnalyticsImpl"
    }
}
