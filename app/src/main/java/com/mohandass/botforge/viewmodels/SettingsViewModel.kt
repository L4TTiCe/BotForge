package com.mohandass.botforge.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.model.preferences.PreferredTheme
import com.mohandass.botforge.model.service.AccountService
import com.mohandass.botforge.model.service.PreferencesDataStore
import com.mohandass.botforge.model.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val sharedPreferencesService: SharedPreferencesService,
    private val preferencesDataStore: PreferencesDataStore,
) : ViewModel() {
    fun getApiKey(): String = sharedPreferencesService.getApiKey()

    fun setApiKey(value: String) {
        sharedPreferencesService.setAPIKey(value)
        SnackbarManager.showMessage(R.string.api_key_saved)
    }
    fun getCurrentUser() = accountService.currentUser

    // Usage
    fun getUsageTokens(): Long {
        return sharedPreferencesService.getUsageTokens()
    }

    fun resetUsageTokens() {
        sharedPreferencesService.resetUsageTokens()
        SnackbarManager.showMessage(R.string.usage_tokens_reset)
    }

    fun updateTheme(preferredTheme: PreferredTheme) {
        viewModelScope.launch {
            preferencesDataStore.updateTheme(preferredTheme)
        }
    }

    fun updateDynamicColor(value: Boolean) {
        Log.v(TAG, "updateDynamicColor() value: $value")
        viewModelScope.launch {
            preferencesDataStore.setDynamicColor(value)
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}