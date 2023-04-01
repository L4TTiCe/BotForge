package com.mohandass.botforge.auth.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mohandass.botforge.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logger: Logger,
) : ViewModel() {
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    private val _passwordVisibility = mutableStateOf(false)

    fun onSignUp(onSuccess: () -> Unit) {
        if (Utils.validateEmail(_email.value).not()) {
            logger.logVerbose(TAG, "onSignUp() email invalid")
            SnackbarManager.showMessage(AppText.email_invalid)
            return
        }

        if (_password.value.length < 6) {
            logger.logVerbose(TAG, "onSignUp() password invalid")
            SnackbarManager.showMessage(AppText.password_invalid)
            return
        }

        if (_password.value != _confirmPassword.value) {
            logger.logVerbose(TAG, "onSignUp() passwords not match")
            SnackbarManager.showMessage(AppText.passwords_not_match)
            return
        }
        logger.logVerbose(TAG, "onSignUp() Authenticating...")

        viewModelScope.launch {

            accountService.createAnonymousAccount()
            accountService.linkAccount(_email.value, _password.value)

            logger.log(TAG, "onSignUp() Authenticated")
            onSuccess()

        }
    }

    val email get() = _email.value
    val password get() = _password.value
    val confirmPassword get() = _confirmPassword.value
    val passwordVisibility get() = _passwordVisibility.value

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun onPasswordVisibilityChange(boolean: Boolean) {
        _passwordVisibility.value = boolean
    }

    companion object {
        private const val TAG = "SignUpViewModel"
    }
}