package com.example.scanpro.domain.model

data class Device(
    val ipAddress: String,
    val name: String,
    val port: Int,
    val isOnline: Boolean,
    val lastSeen: Long
)
