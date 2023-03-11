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
                if (line.contains("message")) {
                    val messageAsJson = "{${line.substring(0, line.length - 1)}}"
                    Log.v("Utils", "parseStackTraceForErrorMessage($messageAsJson)")
                    val map = Gson().fromJson(messageAsJson, Map::class.java)

                    val message = map["message"] as String

                    return if (message.contains("Incorrect API key")) {
                        Throwable("Incorrect API key.")
                    } else {
                        Throwable(message)
                    }
                }
            }

            return Throwable("")
        }
    }
}