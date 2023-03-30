package com.mohandass.botforge.common

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

    fun showMessageWithAction(
        @StringRes message: Int,
        @StringRes dismissLabel: Int,
        dismissAction: () -> Unit = {}
    ) {
        messages.value =
            SnackbarMessage.ResourceSnackbarWithAction(message, dismissLabel, dismissAction)
    }

    fun showMessage(@StringRes message: Int, vararg formatArgs: String) {
        messages.value = SnackbarMessage.ResourceSnackbarWithFormatArg(message, *formatArgs)
    }

    fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }
}