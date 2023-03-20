package com.mohandass.botforge.common

import android.util.Log
import com.google.gson.Gson
import com.mohandass.botforge.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter

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

        fun parseStackTraceForErrorMessage(throwable: Throwable): Throwable {
            val stringWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(stringWriter))

            val stackTrace = stringWriter.toString()
            for (line in stackTrace.split("\n")) {
                if (line.contains(INTERRUPT_ERROR_MESSAGE)) {
                    return Throwable(INTERRUPTED_ERROR_MESSAGE)
                }
                if (line.contains("message")) {
                    val messageAsJson = "{${line.substring(0, line.length - 1)}}"
                    Log.v("Utils", "parseStackTraceForErrorMessage($messageAsJson)")
                    val map = Gson().fromJson(messageAsJson, Map::class.java)

                    val message = map["message"] as String

                    return if (message.contains("Invalid API key")) {
                        Throwable(INVALID_API_KEY_ERROR_MESSAGE)
                    } else {
                        Throwable(message)
                    }
                }
            }

            return Throwable("")
        }

        private const val TAG = "Utils"
        private const val INTERRUPT_ERROR_MESSAGE = "com.mohandass.botforge.chat.ui.viewmodel.ChatViewModel.interruptRequest"
        const val INVALID_API_KEY_ERROR_MESSAGE = "invalid_api_key"
        const val INTERRUPTED_ERROR_MESSAGE = "interrupted"
    }
}