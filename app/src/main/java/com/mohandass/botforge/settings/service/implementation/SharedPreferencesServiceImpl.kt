// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.service.implementation

import android.content.Context
import androidx.core.content.edit
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.settings.service.SharedPreferencesService

/**
 * Implementation of the SharedPreferencesService
 *
 * Uses SharedPreferences to store the user preferences.
 * Used for storing the API key and other settings, that don't other things to be reactive.
 */
class SharedPreferencesServiceImpl private constructor(context: Context) :
    SharedPreferencesService {

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(
            SharedPreferencesService.USER_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

    override fun getApiKey(): String {
        return _apiKey
    }

    private val _apiKey: String
        get() {
            val order = sharedPreferences.getString(SharedPreferencesService.API_KEY, "")
            return order ?: ""
        }

    override fun setAPIKey(apiKey: String) {
        sharedPreferences.edit {
            putString(SharedPreferencesService.API_KEY, apiKey)
        }
    }

    private val _timeout: Int
        get() {
            return sharedPreferences.getInt(SharedPreferencesService.API_TIMEOUT, Constants.DEFAULT_API_TIMEOUT)
        }

    override fun getApiTimeout(): Int {
        return _timeout
    }

    override fun setApiTimeout(timeout: Int) {
        sharedPreferences.edit {
            putInt(SharedPreferencesService.API_TIMEOUT, timeout)
        }
    }

    private val _chatModel: String
        get() {
            return sharedPreferences.getString(SharedPreferencesService.CHAT_MODEL, Constants.DEFAULT_CHAT_MODEL) ?: Constants.DEFAULT_CHAT_MODEL
        }

    override fun getChatModel(): String {
        return _chatModel
    }

    override fun setChatModel(model: String) {
        sharedPreferences.edit {
            putString(SharedPreferencesService.CHAT_MODEL, model)
        }
    }

    private val _usageTokens: Long
        get() {
            return sharedPreferences.getLong(SharedPreferencesService.API_USAGE_AS_TOKENS, 0)
        }

    override fun getUsageTokens(): Long {
        return _usageTokens
    }

    private val _usageImageSmallCount: Long
        get() {
            return sharedPreferences.getLong(
                SharedPreferencesService.API_USAGE_IMAGE_SMALL_COUNT,
                0
            )
        }

    override fun getUsageImageSmallCount(): Long {
        return _usageImageSmallCount
    }

    private val _usageImageMediumCount: Long
        get() {
            return sharedPreferences.getLong(
                SharedPreferencesService.API_USAGE_IMAGE_MEDIUM_COUNT,
                0
            )
        }

    override fun getUsageImageMediumCount(): Long {
        return _usageImageMediumCount
    }

    private val _usageImageLargeCount: Long
        get() {
            return sharedPreferences.getLong(
                SharedPreferencesService.API_USAGE_IMAGE_LARGE_COUNT,
                0
            )
        }

    override fun getUsageImageLargeCount(): Long {
        return _usageImageLargeCount
    }

    override fun incrementUsageTokens(tokens: Int) {
        sharedPreferences.edit {
            putLong(SharedPreferencesService.API_USAGE_AS_TOKENS, _usageTokens + tokens)
        }
    }

    override fun incrementUsageImageSmallCount(count: Int) {
        sharedPreferences.edit {
            putLong(
                SharedPreferencesService.API_USAGE_IMAGE_SMALL_COUNT,
                _usageImageSmallCount + count
            )
        }
    }

    override fun incrementUsageImageMediumCount(count: Int) {
        sharedPreferences.edit {
            putLong(
                SharedPreferencesService.API_USAGE_IMAGE_MEDIUM_COUNT,
                _usageImageMediumCount + count
            )
        }
    }

    override fun incrementUsageImageLargeCount(count: Int) {
        sharedPreferences.edit {
            putLong(
                SharedPreferencesService.API_USAGE_IMAGE_LARGE_COUNT,
                _usageImageLargeCount + count
            )
        }
    }

    override fun resetUsage() {
        sharedPreferences.edit {
            putLong(SharedPreferencesService.API_USAGE_AS_TOKENS, 0)
            putLong(SharedPreferencesService.API_USAGE_IMAGE_SMALL_COUNT, 0)
            putLong(SharedPreferencesService.API_USAGE_IMAGE_MEDIUM_COUNT, 0)
            putLong(SharedPreferencesService.API_USAGE_IMAGE_LARGE_COUNT, 0)
        }
    }

    private val _onBoardingCompleted: Boolean
        get() {
            return sharedPreferences.getBoolean(
                SharedPreferencesService.ON_BOARDING_COMPLETED,
                false
            )
        }

    override fun getOnBoardingCompleted(): Boolean {
        return _onBoardingCompleted
    }

    override fun setOnBoardingCompleted(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(SharedPreferencesService.ON_BOARDING_COMPLETED, value)
        }
    }

    private val _analyticsOptOut: Boolean
        get() {
            return sharedPreferences.getBoolean(SharedPreferencesService.ANALYTICS_OPT_OUT, false)
        }

    override fun getAnalyticsOptOut(): Boolean {
        return _analyticsOptOut
    }

    override fun setAnalyticsOptOut(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(SharedPreferencesService.ANALYTICS_OPT_OUT, value)
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
