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
}