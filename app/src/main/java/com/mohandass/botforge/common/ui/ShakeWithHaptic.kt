// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.mohandass.botforge.common.Constants

// Uses Shake Detector to detect shake and perform haptic feedback
@Composable
fun ShakeWithHaptic(
    shakeSensitivity: Float,
    isEnabled: Boolean = true,
    onShake: () -> Unit,
) {
    if (!isEnabled) return

    val hapticFeedback = LocalHapticFeedback.current

    // Shake threshold is calculated based on the shake sensitivity
    // Higher the sensitivity, lower the threshold
    val shakeThreshold = remember(shakeSensitivity) {
        val threshold = shakeSensitivity - (Constants.MAX_SENSITIVITY_THRESHOLD / 2)
        (threshold * -1) + (Constants.MAX_SENSITIVITY_THRESHOLD / 2)
    }

    ShakeDetector(shakeThreshold = shakeThreshold) {
        onShake()
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }
}
