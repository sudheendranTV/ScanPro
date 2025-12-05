package com.example.scanpro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scanpro.data.dao.DeviceDao
import com.example.scanpro.data.entity.DeviceEntity

@Database(entities = [DeviceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "device_db"
                ).build()
            }
    }
}
