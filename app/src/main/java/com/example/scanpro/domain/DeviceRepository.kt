package com.example.scanpro.domain

import com.example.scanpro.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getDevices(): Flow<List<Device>>
    suspend fun saveDevice(device: Device)
    suspend fun saveAll(devices: List<Device>)
    suspend fun updateStatus(ip: String, online: Boolean)
}
