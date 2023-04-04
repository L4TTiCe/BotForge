package com.mohandass.botforge.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.settings.service.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * A ViewModel to handle the logic for the Landing Screen
 *
 * This class is handles Checking Authentication, OnBoarding, and Sign In
 */
@HiltViewModel
class LandingViewModel @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService,
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

    fun isOnBoardingCompleted(): Boolean {
        return sharedPreferencesService.getOnBoardingCompleted()
    }

    // Creates an anonymous account and signs in
    fun onSkip(onSuccess: () -> Unit) {
        viewModelScope.launch {

            accountService.createAnonymousAccount()

            logger.logVerbose(TAG, "onSkip() Authenticated")
            onSuccess()

        }
    }

    fun onGoogleSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            accountService.signInWithCredential(credential)

            if (accountService.hasUser) {
                logger.logVerbose(TAG, "onGoogleSignIn() Authenticated")
                if (accountService.displayName == "") {
                    logger.logVerbose(TAG, "onGoogleSignIn() New User")
                    accountService.generateAndSetDisplayName()
                }
            }
        }
    }

    companion object {
        private const val TAG = "LandingViewModel"
    }
}
