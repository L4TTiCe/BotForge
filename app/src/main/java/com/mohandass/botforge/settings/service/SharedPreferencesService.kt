// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.service

/**
 * Interface for the SharedPreferencesService
 */
interface SharedPreferencesService {

    // API Key
    fun getApiKey(): String
    fun setAPIKey(apiKey: String)

    // OnBoarding
    fun getOnBoardingCompleted(): Boolean
    fun setOnBoardingCompleted(value: Boolean)

    // Usage
    fun getUsageTokens(): Long
    fun incrementUsageTokens(tokens: Int)
    fun resetUsageTokens()

    // Analytics
    fun getAnalyticsOptOut(): Boolean
    fun setAnalyticsOptOut(value: Boolean)

    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
        const val API_KEY = "open_ai_api_key"
        const val API_USAGE_AS_TOKENS = "open_ai_api_usage_tokens"
        const val ON_BOARDING_COMPLETED = "on_boarding_completed"
        const val ANALYTICS_OPT_OUT = "analytics_opt_out"
    }
}
