// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services.snackbar

/**
 * An enum class that holds all possible locations for a snackbar.
 * This is used to determine which snackbar to show when multiple snackbar launchers are present.
 *
 * Eg. If a snackbar is launched from the Chat screen, it should be shown on the Chat Host.
 * and not on the Main Host, as Chat has a FAB, and the snackbar should be shown above the FAB.
 */
enum class SnackbarLauncherLocation {
    MAIN,
    CHAT,
}
