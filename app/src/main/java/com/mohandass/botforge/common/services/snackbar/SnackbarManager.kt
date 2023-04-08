// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services.snackbar

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Derived from
// https://github.com/FirebaseExtended/make-it-so-android

/**
 * A to class simplifies showing a snackbar.
 *
 * Each instance of this class is associated with a specific [SnackbarLauncherLocation],
 * and will hold the message to be shown on that location.
 *
 * Usage:
 * SnackbarManager.showMessage(R.string.message)
 * SnackbarManager.showMessage(R.string.message, "arg1", "arg2") // formatArgs
 * SnackbarManager.showMessageWithAction(R.string.message, R.string.dismiss) { // dismissAction }
 */
class SnackbarManager {
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun clearMessage() {
        messages.value = null
    }

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

    companion object {
        private val instances = mutableMapOf<String, SnackbarManager>()
        private var activeSnackbar by mutableStateOf(SnackbarLauncherLocation.MAIN)

        // Set where the snackbar messages should be routed to.
        fun setSnackbarLocation(location: SnackbarLauncherLocation) {
            activeSnackbar = location
        }

        // Get the snackbar manager for the passed in location.
        fun getInstance(location: SnackbarLauncherLocation): SnackbarManager {
            val key = location.name
            if (!instances.containsKey(key)) {
                instances[key] = SnackbarManager()
            }
            return instances[key]!!
        }

        // Get the snackbar manager for the active location.
        fun getInstance(): SnackbarManager {
            val key = activeSnackbar.name
            if (!instances.containsKey(key)) {
                instances[key] = SnackbarManager()
            }
            return instances[key]!!
        }

        // Clear the snackbar message, if any.
        // This prevents the same snackbar from showing up again.
        fun clearMessage() {
            getInstance().clearMessage()
        }

        fun showMessage(message: SnackbarMessage) {
            getInstance().showMessage(message)
        }

        // Forwards the message to the active snackbar manager.
        fun showMessage(@StringRes message: Int) {
            getInstance().showMessage(message)
        }

        fun showMessage(@StringRes message: Int, vararg formatArgs: String) {
            getInstance().showMessage(message, *formatArgs)
        }

        fun showMessageWithAction(
            @StringRes message: Int,
            @StringRes dismissLabel: Int,
            dismissAction: () -> Unit = {}
        ) {
            getInstance().showMessageWithAction(message, dismissLabel, dismissAction)
        }

    }
}