package com.example.scanpro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanpro.domain.LoginWithGoogleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginWithGoogleUseCase) : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun onGoogleLoginSuccess(idToken: String) {
        viewModelScope.launch {
            loginUseCase(idToken)
            _loginSuccess.value = true
        }
    }
}