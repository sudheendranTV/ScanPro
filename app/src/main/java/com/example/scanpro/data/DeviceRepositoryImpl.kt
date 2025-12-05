package com.example.scanpro.data

import com.example.scanpro.data.dao.DeviceDao
import com.example.scanpro.domain.DeviceRepository
import com.example.scanpro.domain.model.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.InetSocketAddress
import java.net.Socket

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

    override suspend fun updateDevice(device: Device) {
        dao.updateDevice(device.toEntity())
    }
    override suspend fun isDeviceReachable(host: String): Boolean {
     /* val result =  CoroutineScope(Dispatchers.IO).async{
            try {
                val address = InetAddress.getByName(host)
                address.isReachable(1500) // 1.5 sec timeout
                return@async
            } catch (e: Exception) {
                false
            }
        }.await()
        return result*/
        return true
    }

    override suspend fun isDeviceReachable(ip: String, port: Int, timeout: Int): Boolean {
        val result = CoroutineScope(Dispatchers.IO).async {
            try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(ip, port), timeout)
                    return@use true
                }
            } catch (e: Exception) {
                false
            }
        }.await()
        return result
    }


}
