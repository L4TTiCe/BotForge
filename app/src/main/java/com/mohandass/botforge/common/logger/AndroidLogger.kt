package com.mohandass.botforge.common.logger

import android.util.Log

class AndroidLogger: Logger {
    override fun log(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun logVerbose(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun logError(tag:String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }
}