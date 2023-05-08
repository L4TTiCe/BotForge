// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.services.Analytics
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.OpenAiService
import com.mohandass.botforge.settings.model.internal.ModelInternal
import com.mohandass.botforge.settings.model.toInternal
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvancedApiSettingsViewModel @Inject constructor(
    private val openAiService: OpenAiService,
    private val sharedPreferencesService: SharedPreferencesService,
    private val logger: Logger,
    private val analytics: Analytics,
) : ViewModel() {
    val apiTimeout = mutableStateOf(sharedPreferencesService.getApiTimeout())

    val selectedModel = mutableStateOf(sharedPreferencesService.getChatModel())
    val availableModels = mutableStateOf(emptyList<ModelInternal>())

    fun getAvailableModels() {
        logger.log(TAG, "getAvailableModels()")
        viewModelScope.launch {
            try {
                val models = openAiService.getAvailableModels()

                availableModels.value = models.filter {
                        it.id.id.contains("gpt", ignoreCase = true)
                    }.map {
                        it.toInternal()
                    }

                for (model in availableModels.value) {
                    logger.logVerbose(TAG, "getAvailableModels() ${model.model.id.id}")
                }
            } catch (e: Exception) {
                logger.logError(TAG, "getAvailableModels()", e)
            }
        }
    }

    fun setApiTimeout() {
        logger.log(TAG, "setApiTimeout()")
        sharedPreferencesService.setApiTimeout(apiTimeout.value)
    }

    fun setChatModel() {
        logger.log(TAG, "setChatModel()")
        sharedPreferencesService.setChatModel(selectedModel.value)
    }

    companion object {
        private const val TAG = "AdvancedApiSettingsViewModel"
    }
}