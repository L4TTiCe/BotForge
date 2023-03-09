package com.mohandass.botforge.common

import android.util.Log
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackbarManager {
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun showMessage(@StringRes message: Int) {
        messages.value = SnackbarMessage.ResourceSnackbar(message)
    }

    fun showMessage(@StringRes message: Int, vararg formatArgs: String) {
        Log.v("SnackbarManager", "showMessage($message, $formatArgs)")
        messages.value = SnackbarMessage.ResourceSnackbarWithFormatArg(message, *formatArgs)
    }

    fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }
}