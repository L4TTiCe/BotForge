package com.mohandass.botforge.common.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashlyticsLogger: Logger {
    override fun log(tag: String, message: String) {
        FirebaseCrashlytics.getInstance().log("$tag: $message")
    }

    override fun logVerbose(tag: String, message: String) {
        FirebaseCrashlytics.getInstance().log("$tag: $message")
    }

    override fun logError(tag: String, message: String) {
        FirebaseCrashlytics.getInstance().log("$tag: $message")
    }

    override fun logError(tag:String, message: String, throwable: Throwable) {
        FirebaseCrashlytics.getInstance().log("$tag: $message")
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}
