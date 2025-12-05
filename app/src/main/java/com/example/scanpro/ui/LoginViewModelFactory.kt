package com.example.scanpro.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scanpro.data.AuthRepositoryImpl
import com.example.scanpro.data.TokenStore
import com.example.scanpro.domain.LoginWithGoogleUseCase

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val tokenStore = TokenStore(context)
        val repository = AuthRepositoryImpl(tokenStore)
        val useCase = LoginWithGoogleUseCase(repository)

        return LoginViewModel(useCase) as T
    }
}