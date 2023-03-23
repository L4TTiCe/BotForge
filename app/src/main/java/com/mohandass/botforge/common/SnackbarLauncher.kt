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
                    )
                }
            }
        }
    }
}
