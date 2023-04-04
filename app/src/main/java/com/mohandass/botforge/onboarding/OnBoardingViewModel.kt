package com.mohandass.botforge.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.PreferencesDataStore
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the OnBoarding screen
 *
 * Helps the users to set their API key and other settings before using the app
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService,
    private val preferencesDataStore: PreferencesDataStore,
    private val logger: Logger,
) : ViewModel() {
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