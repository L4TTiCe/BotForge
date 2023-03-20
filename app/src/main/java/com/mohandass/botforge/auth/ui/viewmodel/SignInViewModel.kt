package com.mohandass.botforge.auth.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.auth.model.services.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mohandass.botforge.R.string as AppText

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logger: Logger,
) : ViewModel() {
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _passwordVisibility = mutableStateOf(false)

    fun signIn(onSuccess: () -> Unit) {
        logger.log(TAG, "signIn()")

        _email.value = _email.value.trim()

        if (Utils.validateEmail(email).not()) {
            SnackbarManager.showMessage(AppText.email_invalid)
            logger.logVerbose(TAG, "signIn() email invalid")
            return
        }
        if (password.length < 6) {
            SnackbarManager.showMessage(AppText.password_invalid)
            logger.logVerbose(TAG, "signIn() password invalid")
            return
        }

        viewModelScope.launch {
            logger.logVerbose(TAG, "signIn() Authenticating...")
            try {
                accountService.authenticate(email, password)
            } catch (e: Exception) {
                logger.logVerbose(TAG, "signIn() Authentication failed")
                SnackbarManager.showMessage(AppText.wrong_credentials)
                return@launch
            }
            logger.logVerbose(TAG, "signIn() Authenticated")
            onSuccess()
        }
    }

    fun sendRecoveryEmail() {
        logger.log(TAG, "sendRecoveryEmail()")

        _email.value = _email.value.trim()

        if (Utils.validateEmail(email).not()) {
            SnackbarManager.showMessage(AppText.email_invalid)
            logger.logVerbose(TAG, "sendRecoveryEmail() email invalid")
            return
        }

        viewModelScope.launch {
            if (Utils.validateEmail(email).not()) {
                SnackbarManager.showMessage(AppText.email_invalid)
                logger.logVerbose(TAG, "sendRecoveryEmail() email invalid")
                return@launch
            }
            logger.logVerbose(TAG, "sendRecoveryEmail() Sending recovery email...")
            try {
                accountService.sendRecoveryEmail(email)
            } catch (e: Exception) {
                logger.logVerbose(TAG, "sendRecoveryEmail() Sending recovery email failed")
                SnackbarManager.showMessage(AppText.generic_error)
                return@launch
            }
            logger.logVerbose(TAG, "sendRecoveryEmail() Recovery email sent")
            SnackbarManager.showMessage(AppText.recovery_email_sent)
        }
    }

    val email get() = _email.value
    val password get() = _password.value
    val passwordVisibility get() = _passwordVisibility.value

    fun onEmailChange(email: String) {
        _email.value = email
    }
    fun onPasswordChange(password: String) {
        _password.value = password
    }
    fun onPasswordVisibilityChange(boolean: Boolean) {
        _passwordVisibility.value = boolean
    }

    companion object {
        private const val TAG = "SignInViewModel"
    }
}