package com.mohandass.botforge.settings.service.implementation

import android.content.Context
import androidx.core.content.edit
import com.mohandass.botforge.settings.service.SharedPreferencesService

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val API_KEY = "open_ai_api_key"
private const val API_USAGE_AS_TOKENS = "open_ai_api_usage_tokens"
private const val ON_BOARDING_COMPLETED = "on_boarding_completed"

/**
 * Implementation of the SharedPreferencesService
 *
 * Uses SharedPreferences to store the user preferences.
 * Used for storing the API key and other settings, that don't other things to be reactive.
 */
class SharedPreferencesServiceImpl private constructor(context: Context) :
    SharedPreferencesService {

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

    private val _onBoardingCompleted: Boolean
        get() {
            return sharedPreferences.getBoolean(ON_BOARDING_COMPLETED, false)
        }

    override fun getOnBoardingCompleted(): Boolean {
        return _onBoardingCompleted
    }

    override fun setOnBoardingCompleted(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(ON_BOARDING_COMPLETED, value)
        }
    }

    companion object {
        private const val TAG = "SharedPreferencesImpl"

        @Volatile
        private var INSTANCE: SharedPreferencesService? = null

        fun getInstance(applicationContext: Context): SharedPreferencesService {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = SharedPreferencesServiceImpl(applicationContext)
                INSTANCE = instance
                return instance
            }
        }
    }
}