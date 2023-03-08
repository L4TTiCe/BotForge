package com.mohandass.botforge.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.mohandass.botforge.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    private val _passwordVisibility = mutableStateOf(false)

    fun onSignUp(onSuccess: () -> Unit) {
        if(Utils.validateEmail(_email.value).not()) {
            Log.v("SignUpViewModel", "onSignUp() email invalid")
            SnackbarManager.showMessage(AppText.email_invalid)
            return
        }

        if(_password.value.length < 6) {
            Log.v("SignUpViewModel", "onSignUp() password invalid")
            SnackbarManager.showMessage(AppText.password_invalid)
            return
        }

        if(_password.value != _confirmPassword.value) {
            Log.v("SignUpViewModel", "onSignUp() passwords not match")
            SnackbarManager.showMessage(AppText.passwords_not_match)
            return
        }
        Log.v("SignUpViewModel", "onSignUp() Authenticating...")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                accountService.createAnonymousAccount()
                accountService.linkAccount(_email.value, _password.value)
            }
            withContext(Dispatchers.Main) {
                Log.v("SignUpViewModel", "onSignUp() Authenticated")
                onSuccess()
            }
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
}