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
    fun getApiTimeout(): Int
    fun setApiTimeout(timeout: Int)

    // OnBoarding
    fun getOnBoardingCompleted(): Boolean
    fun setOnBoardingCompleted(value: Boolean)

    // Usage
    fun getUsageTokens(): Long
    fun getUsageImageSmallCount(): Long
    fun getUsageImageMediumCount(): Long
    fun getUsageImageLargeCount(): Long
    fun incrementUsageTokens(tokens: Int)
    fun incrementUsageImageSmallCount(count: Int)
    fun incrementUsageImageMediumCount(count: Int)
    fun incrementUsageImageLargeCount(count: Int)
    fun resetUsage()

    // Analytics
    fun getAnalyticsOptOut(): Boolean
    fun setAnalyticsOptOut(value: Boolean)

    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
        const val API_KEY = "open_ai_api_key"
        const val API_TIMEOUT = "open_ai_api_timeout"
        const val API_USAGE_AS_TOKENS = "open_ai_api_usage_tokens"
        const val API_USAGE_IMAGE_SMALL_COUNT = "open_ai_api_usage_image_small_count"
        const val API_USAGE_IMAGE_MEDIUM_COUNT = "open_ai_api_usage_image_medium_count"
        const val API_USAGE_IMAGE_LARGE_COUNT = "open_ai_api_usage_image_large_count"
        const val ON_BOARDING_COMPLETED = "on_boarding_completed"
        const val ANALYTICS_OPT_OUT = "analytics_opt_out"
    }
}
