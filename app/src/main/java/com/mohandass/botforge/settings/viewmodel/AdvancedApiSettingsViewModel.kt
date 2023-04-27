// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdvancedApiSettingsViewModel @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService,
    private val logger: Logger,
    private val analytics: Analytics,
) : ViewModel() {
    val apiTimeout = mutableStateOf(sharedPreferencesService.getApiTimeout())

    fun setApiTimeout() {
        logger.log(TAG, "setApiTimeout()")
        sharedPreferencesService.setApiTimeout(apiTimeout.value)
    }

    companion object {
        private const val TAG = "AdvancedApiSettingsViewModel"
    }
}