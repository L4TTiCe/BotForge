package com.mohandass.botforge.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    fun checkAuthentication(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (accountService.hasUser) {
                    Log.v("LandingViewModel", "checkAuthentication() Authenticated")
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
                Log.v("LandingViewModel", "onSkip() Authenticated")
                onSuccess()
            }
        }
    }
}
