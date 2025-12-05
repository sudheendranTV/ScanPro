package com.example.scanpro.data

import com.example.scanpro.data.dao.DeviceDao
import com.example.scanpro.domain.DeviceRepository
import com.example.scanpro.domain.model.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeviceRepositoryImpl(private val dao: DeviceDao) : DeviceRepository {

    override fun getDevices(): Flow<List<Device>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun saveDevice(device: Device) {
        dao.insert(device.toEntity())
    }

    override suspend fun saveAll(devices: List<Device>) {
        dao.insertAll(devices.map { it.toEntity() })
    }

    override suspend fun updateStatus(ip: String, online: Boolean) {
        dao.updateStatus(ip, online, System.currentTimeMillis())
    }
}
