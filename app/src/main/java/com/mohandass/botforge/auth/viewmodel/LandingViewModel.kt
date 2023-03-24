package com.mohandass.botforge.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.auth.model.services.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logger: Logger,
) : ViewModel() {

    fun checkAuthentication(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (accountService.hasUser) {
                    logger.logVerbose(TAG, "checkAuthentication() Authenticated")
                    onSuccess()
                }
            }
        }
    }

    fun onSkip(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                accountService.createAnonymousAccount()
            }
            withContext(Dispatchers.Main) {
                logger.logVerbose(TAG, "onSkip() Authenticated")
                onSuccess()
            }
        }
    }

    companion object {
        private const val TAG = "LandingViewModel"
    }
}
