package com.mohandass.botforge.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.mohandass.botforge.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    fun getCurrentUser() = accountService.currentUser

}