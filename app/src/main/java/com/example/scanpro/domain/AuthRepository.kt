package com.example.scanpro.domain

interface AuthRepository {
    suspend fun saveToken(token: String)
}