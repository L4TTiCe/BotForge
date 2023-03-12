package com.mohandass.botforge.model.service.implementation

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.mohandass.botforge.model.service.DataStoreService

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val API_KEY = "open_ai_api_key"
private const val API_USAGE_AS_TOKENS = "open_ai_api_usage_tokens"

class DataStoreServiceImpl private constructor(context: Context) : DataStoreService{

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getApiKey(): String {
        return _apiKey
    }

    private val _apiKey: String
        get() {
            val order = sharedPreferences.getString(API_KEY, "")
            return order ?: ""
        }

    override fun setAPIKey(apiKey: String) {
        Log.v("DataStoreImpl", "setAPIKey() $apiKey")
        sharedPreferences.edit {
            putString(API_KEY, apiKey)
        }
    }

    override fun getUsageTokens(): Long {
        return _usageTokens
    }

    private val _usageTokens: Long
        get() {
            return sharedPreferences.getLong(API_USAGE_AS_TOKENS, 0)
        }

    override fun incrementUsageTokens(tokens: Int) {
        sharedPreferences.edit {
            putLong(API_USAGE_AS_TOKENS, _usageTokens + tokens)
        }
    }

    override fun resetUsageTokens() {
        sharedPreferences.edit {
            putLong(API_USAGE_AS_TOKENS, 0)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoreService? = null

        fun getInstance(applicationContext: Context): DataStoreService {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = DataStoreServiceImpl(applicationContext)
                INSTANCE = instance
                return instance
            }
        }
    }
}