package com.mohandass.botforge.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
import com.mohandass.botforge.settings.model.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val sharedPreferencesService: SharedPreferencesService,
    private val preferencesDataStore: PreferencesDataStore,
    private val logger: Logger,
) : ViewModel() {
    fun getApiKey(): String = sharedPreferencesService.getApiKey()

    fun setApiKey(value: String) {
        logger.log(TAG, "setApiKey()")
        sharedPreferencesService.setAPIKey(value)
        SnackbarManager.showMessage(R.string.api_key_saved)
    }
    fun getCurrentUser() = accountService.currentUser

    // Usage
    fun getUsageTokens(): Long {
        logger.logVerbose(TAG, "getUsageTokens()")
        return sharedPreferencesService.getUsageTokens()
    }

    fun resetUsageTokens() {
        logger.log(TAG, "resetUsageTokens()")
        sharedPreferencesService.resetUsageTokens()
        SnackbarManager.showMessage(R.string.usage_tokens_reset)
    }

    fun updateTheme(preferredTheme: PreferredTheme) {
        logger.log(TAG, "updateTheme() preferredTheme: $preferredTheme")
        viewModelScope.launch {
            preferencesDataStore.updateTheme(preferredTheme)
        }
    }

    fun updateDynamicColor(value: Boolean) {
        logger.log(TAG, "updateDynamicColor() value: $value")
        viewModelScope.launch {
            preferencesDataStore.setDynamicColor(value)
        }
    }

    fun clearLastSyncTime() {
        logger.log(TAG, "clearLastSyncTime()")
        viewModelScope.launch {
            preferencesDataStore.clearLastSuccessfulSync()
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}