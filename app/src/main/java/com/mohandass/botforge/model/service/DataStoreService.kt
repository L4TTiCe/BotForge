package com.mohandass.botforge.model.service

interface DataStoreService {
    fun getApiKey(): String
    fun setAPIKey(apiKey: String)
}