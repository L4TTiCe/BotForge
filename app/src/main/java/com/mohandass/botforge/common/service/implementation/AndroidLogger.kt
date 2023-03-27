package com.mohandass.botforge.common.service.implementation

import android.util.Log
import com.mohandass.botforge.common.service.Logger

class AndroidLogger : Logger {
    override fun log(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun logVerbose(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun logError(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }
}
