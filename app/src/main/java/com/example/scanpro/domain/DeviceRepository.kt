package com.example.scanpro.domain

import com.example.scanpro.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getDevices(): Flow<List<Device>>
    suspend fun saveDevice(device: Device)
    suspend fun saveAll(devices: List<Device>)
    suspend fun updateStatus(ip: String, online: Boolean)
    suspend fun updateDevice(device: Device)
    suspend fun isDeviceReachable(host: String): Boolean
    suspend fun isDeviceReachable(ip: String, port: Int = 80, timeout: Int = 1500): Boolean
}
