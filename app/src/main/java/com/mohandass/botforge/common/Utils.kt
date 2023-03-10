package com.mohandass.botforge.common

import com.mohandass.botforge.BuildConfig

class Utils {
    companion object {
        fun validateEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun getAppVersion(): String {
            return BuildConfig.VERSION_NAME
        }

        fun getAppVersionCode(): Int {
            return BuildConfig.VERSION_CODE
        }
    }
}