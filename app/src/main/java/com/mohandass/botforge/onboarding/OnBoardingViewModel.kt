package com.mohandass.botforge.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
import com.mohandass.botforge.settings.model.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService,
    private val preferencesDataStore: PreferencesDataStore,
    private val logger: Logger,
): ViewModel() {
    fun getApiKey(): String = sharedPreferencesService.getApiKey()

    fun setApiKey(value: String) {
        logger.log(TAG, "setApiKey()")
        sharedPreferencesService.setAPIKey(value)
    }

    fun setUserGeneratedContent(value: Boolean) {
        logger.log(TAG, "setUserGeneratedContent() value: $value")
        viewModelScope.launch {
            preferencesDataStore.setUserGeneratedContent(value)
        }
    }

    fun setOnBoardingCompleted() {
        logger.log(TAG, "setOnBoardingCompleted()")
        sharedPreferencesService.setOnBoardingCompleted(true)
    }

    companion object {
        private const val TAG = "OnBoardingViewModel"
    }
}