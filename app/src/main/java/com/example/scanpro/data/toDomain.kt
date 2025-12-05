package com.example.scanpro.data

import com.example.scanpro.data.entity.DeviceEntity
import com.example.scanpro.domain.model.Device

fun DeviceEntity.toDomain() = Device(ipAddress, name, port,isOnline, lastSeen)
fun Device.toEntity() = DeviceEntity(name, ipAddress,port, isOnline, lastSeen)
