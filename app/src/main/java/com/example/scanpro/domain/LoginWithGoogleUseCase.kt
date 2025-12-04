package com.example.scanpro.domain


class LoginWithGoogleUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(idToken: String) = repository.saveToken(idToken)
}
