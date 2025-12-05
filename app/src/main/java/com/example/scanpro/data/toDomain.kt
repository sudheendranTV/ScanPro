package com.example.scanpro.data

import com.example.scanpro.data.entity.DeviceEntity
import com.example.scanpro.domain.model.Device

fun DeviceEntity.toDomain() = Device(ipAddress, name, isOnline, lastSeen)
fun Device.toEntity() = DeviceEntity(ipAddress, name, isOnline, lastSeen)
