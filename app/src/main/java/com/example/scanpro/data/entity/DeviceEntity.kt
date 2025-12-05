    package com.example.scanpro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey val ipAddress: String,
    val name: String,
    val isOnline: Boolean,
    val lastSeen: Long
)
