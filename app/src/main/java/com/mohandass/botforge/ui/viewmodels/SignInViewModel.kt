package com.mohandass.botforge.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.model.Utils
import com.mohandass.botforge.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mohandass.botforge.R.string as AppText

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService)
    : ViewModel() {
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _passwordVisibility = mutableStateOf(false)

    fun signIn(onSuccess: () -> Unit) {
        Log.v("SignInViewModel", "signIn()")

        _email.value = _email.value.trim()

        if (Utils.validateEmail(email).not()) {
            SnackbarManager.showMessage(AppText.email_invalid)
            Log.v("SignInViewModel", "signIn() email invalid")
            return
        }
        if (password.length < 6) {
            SnackbarManager.showMessage(AppText.password_invalid)
            Log.v("SignInViewModel", "signIn() password invalid")
            return
        }

        viewModelScope.launch {
            Log.v("SignInViewModel", "signIn() Authenticating...")
            try {
                accountService.authenticate(email, password)
            } catch (e: Exception) {
                Log.v("SignInViewModel", "signIn() Authentication failed")
                SnackbarManager.showMessage(AppText.wrong_credentials)
                return@launch
            }
            Log.v("SignInViewModel", "signIn() Authenticated")
            onSuccess()
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
}