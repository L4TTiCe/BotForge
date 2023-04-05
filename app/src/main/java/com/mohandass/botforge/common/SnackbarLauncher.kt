// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common

import android.content.res.Resources
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Stable
import com.mohandass.botforge.common.SnackbarMessage.Companion.getDismissAction
import com.mohandass.botforge.common.SnackbarMessage.Companion.getDismissLabel
import com.mohandass.botforge.common.SnackbarMessage.Companion.hasAction
import com.mohandass.botforge.common.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

// Derived from
// https://github.com/FirebaseExtended/make-it-so-android

/**
 * A class that launches a snackbar when a [SnackbarMessage] is received on [SnackbarManager.snackbarMessages].
 *
 * @param snackbarHostState The [SnackbarHostState] that will be used to show the snackbar.
 * @param snackbarManager The [SnackbarManager] that will be used to observe [SnackbarMessage]s.
 * @param resources The [Resources] that will be used to resolve the [SnackbarMessage]s.
 * @param coroutineScope The [CoroutineScope] that will be used to launch the snackbar.
 */
@Stable
class SnackbarLauncher(
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager = SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                val hasDismissAction = snackbarMessage.hasAction()
                if (hasDismissAction) {
                    val dismissLabel = snackbarMessage.getDismissLabel(resources)
                    val dismissAction = snackbarMessage.getDismissAction()

                    val result: SnackbarResult = snackbarHostState.showSnackbar(
                        message = text,
                        actionLabel = dismissLabel,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        dismissAction()
                    }
                } else {
                    snackbarHostState.showSnackbar(
                        text,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )
                }
            }
        }
    }
}
