package com.mohandass.botforge.model.service

interface SharedPreferencesService {

    // API Key
    fun getApiKey(): String
    fun setAPIKey(apiKey: String)

    // Usage
    fun getUsageTokens(): Long
    fun incrementUsageTokens(tokens: Int)
    fun resetUsageTokens()
}