package com.mohandass.botforge.viewmodels

import androidx.lifecycle.ViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.model.service.AccountService
import com.mohandass.botforge.model.service.DataStoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val dataStoreService: DataStoreService
) : ViewModel() {

    fun getApiKey() = dataStoreService.getApiKey()

    fun setApiKey(value: String) {
        dataStoreService.setAPIKey(value)
        SnackbarManager.showMessage(R.string.api_key_saved)
    }
    fun getCurrentUser() = accountService.currentUser

}