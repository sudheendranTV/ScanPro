package com.example.scanpro.data.dao

import androidx.room.*
import com.example.scanpro.data.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    @Query("SELECT * FROM devices")
    fun getAll(): Flow<List<DeviceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: DeviceEntity)
    @Query("Delete from devices")
    suspend fun deleteStoredDevices()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<DeviceEntity>)

    @Query("UPDATE devices SET isOnline = :online, lastSeen = :time WHERE ipAddress = :ip")
    suspend fun updateStatus(ip: String, online: Boolean, time: Long)

    @Update
    suspend fun updateDevice(device: DeviceEntity)
}
