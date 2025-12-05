package com.example.scanpro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey
    val name: String,
    val ipAddress: String,
    val port: Int,
    val isOnline: Boolean,
    val lastSeen: Long
)
