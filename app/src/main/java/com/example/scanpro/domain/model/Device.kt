package com.example.scanpro.domain.model

data class Device(
    val ipAddress: String,
    val name: String,
    val isOnline: Boolean,
    val lastSeen: Long
)
