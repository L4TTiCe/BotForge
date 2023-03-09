package com.mohandass.botforge.common

import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import com.mohandass.botforge.R.string as AppText

sealed class SnackbarMessage {
    class StringSnackbar(val message: String) : SnackbarMessage()
    class ResourceSnackbar(@StringRes val message: Int) : SnackbarMessage()
    class ResourceSnackbarWithFormatArg(@StringRes val message: Int, vararg formatArgs: String) : SnackbarMessage() {
        val formatArg: List<String> = formatArgs.toList()
    }

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String {
            val message = when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> resources.getString(this.message)
                is ResourceSnackbarWithFormatArg -> {
                    Log.v("SnackbarMessage", "toMessage($message, $formatArg)")
                    resources.getString(this.message, *this.formatArg.toTypedArray())
                }
            }

            Log.v("SnackbarMessage", "toMessage($message)")
            return message
        }

        fun Throwable.toSnackbarMessage(): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbar(message)
            else ResourceSnackbar(AppText.generic_error)
        }
    }
}
