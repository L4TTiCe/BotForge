package com.mohandass.botforge.common

import android.content.res.Resources
import androidx.annotation.StringRes
import com.mohandass.botforge.R.string as AppText

sealed class SnackbarMessage {
    class StringSnackbar(val message: String) : SnackbarMessage()
    class ResourceSnackbar(@StringRes val message: Int) : SnackbarMessage()
    class ResourceSnackbarWithFormatArg(@StringRes val message: Int, vararg formatArgs: String) :
        SnackbarMessage() {
        val formatArg: List<String> = formatArgs.toList()
    }

    class StringSnackbarWithAction(
        val message: String,
        @StringRes val dismissLabel: Int,
        val dismissAction: () -> Unit = {}
    ) : SnackbarMessage()

    class ResourceSnackbarWithAction(
        @StringRes val message: Int,
        @StringRes val dismissLabel: Int,
        val dismissAction: () -> Unit = {}
    ) : SnackbarMessage()

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String {
            val message = when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> resources.getString(this.message)
                is ResourceSnackbarWithFormatArg -> {
                    resources.getString(this.message, *this.formatArg.toTypedArray())
                }

                is StringSnackbarWithAction -> this.message
                is ResourceSnackbarWithAction -> resources.getString(this.message)
            }

            return message
        }

        fun SnackbarMessage.hasAction(): Boolean {
            return when (this) {
                is StringSnackbar -> false
                is ResourceSnackbar -> false
                is ResourceSnackbarWithFormatArg -> false

                is StringSnackbarWithAction -> true
                is ResourceSnackbarWithAction -> true
            }
        }

        fun SnackbarMessage.getDismissAction(): () -> Unit {
            return when (this) {
                is StringSnackbar -> {
                    {}
                }
                is ResourceSnackbar -> {
                    {}
                }
                is ResourceSnackbarWithFormatArg -> {
                    {}
                }

                is StringSnackbarWithAction -> this.dismissAction
                is ResourceSnackbarWithAction -> this.dismissAction
            }
        }

        fun SnackbarMessage.getDismissLabel(resources: Resources): String {
            return when (this) {
                is StringSnackbar -> ""
                is ResourceSnackbar -> ""
                is ResourceSnackbarWithFormatArg -> ""

                is StringSnackbarWithAction -> resources.getString(this.dismissLabel)
                is ResourceSnackbarWithAction -> resources.getString(this.dismissLabel)
            }
        }

        fun Throwable.toSnackbarMessage(): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbar(message)
            else ResourceSnackbar(AppText.generic_error)
        }

        fun Throwable.toSnackbarMessageWithAction(
            @StringRes dismissLabel: Int,
            dismissAction: () -> Unit
        ): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbarWithAction(
                message,
                dismissLabel,
                dismissAction
            )
            else ResourceSnackbarWithAction(AppText.generic_error, dismissLabel, dismissAction)
        }

        fun Exception.toSnackbarMessageWithAction(
            @StringRes dismissLabel: Int,
            dismissAction: () -> Unit
        ): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbarWithAction(
                message,
                dismissLabel,
                dismissAction
            )
            else ResourceSnackbarWithAction(AppText.generic_error, dismissLabel, dismissAction)
        }
    }
}
