package com.example.scanpro.data

import com.example.scanpro.domain.AuthRepository


class AuthRepositoryImpl(private val tokenStore: TokenStore) : AuthRepository {
    override suspend fun saveToken(token: String) {
        tokenStore.saveAccessToken(token)
    }
}
