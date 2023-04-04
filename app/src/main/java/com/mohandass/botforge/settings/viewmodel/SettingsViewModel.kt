package com.mohandass.botforge.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.SnackbarMessage
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.model.PreferredTheme
import com.mohandass.botforge.settings.service.PreferencesDataStore
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen
 *
 * Handles various settings for the app, such as preferred theme, API key, Account, etc.
 */
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

    fun getDisplayName() = accountService.displayName

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

    fun setUserGeneratedContent(value: Boolean) {
        logger.log(TAG, "setUserGeneratedContent() value: $value")
        viewModelScope.launch {
            preferencesDataStore.setUserGeneratedContent(value)
        }
    }

    fun setShakeToClear(value: Boolean) {
        logger.log(TAG, "setShakeToClear() value: $value")
        viewModelScope.launch {
            preferencesDataStore.setShakeToClear(value)
        }
    }

    fun setShakeToClearSensitivity(value: Float) {
        logger.log(TAG, "setShakeToClearSensitivity() value: $value")
        viewModelScope.launch {
            preferencesDataStore.setShakeToClearSensitivity(value)
        }
    }

    fun regenerateDisplayName() {
        logger.log(TAG, "regenerateDisplayName()")
        viewModelScope.launch {
            accountService.generateAndSetDisplayName()
        }
    }

    fun onGoogleSignIn(credential: AuthCredential, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                accountService.linkWithCredential(credential)
                SnackbarManager.showMessage(R.string.account_linked)
                onSuccess()
            } catch (e: Exception) {
                SnackbarManager.showMessage(
                    SnackbarMessage.StringSnackbar(
                        e.message ?: "Error linking account"
                    )
                )
            }
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}