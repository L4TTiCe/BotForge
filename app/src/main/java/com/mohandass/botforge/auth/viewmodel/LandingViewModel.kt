package com.mohandass.botforge.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.common.service.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logger: Logger,
) : ViewModel() {

    fun checkAuthentication(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (accountService.hasUser) {
                logger.logVerbose(TAG, "checkAuthentication() Authenticated")
                onSuccess()
            }
        }
    }

    fun onSkip(onSuccess: () -> Unit) {
        viewModelScope.launch {

            accountService.createAnonymousAccount()

            logger.logVerbose(TAG, "onSkip() Authenticated")
            onSuccess()

        }
    }

    companion object {
        private const val TAG = "LandingViewModel"
    }
}
