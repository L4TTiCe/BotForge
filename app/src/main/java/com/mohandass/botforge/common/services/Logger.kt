// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

/**
 * An interface to abstract the logging mechanism
 */
interface Logger {
    fun log(tag: String, message: String)
    fun logVerbose(tag: String, message: String)
    fun logError(tag: String, message: String)
    fun logError(tag: String, message: String, throwable: Throwable)
}
