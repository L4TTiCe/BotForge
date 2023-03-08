package com.mohandass.botforge.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class SignUpViewModel: ViewModel() {
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    private val _passwordVisibility = mutableStateOf(false)

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