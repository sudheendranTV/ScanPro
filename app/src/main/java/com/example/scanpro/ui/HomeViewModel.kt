package com.example.scanpro.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanpro.data.MdnsDiscoveryManager
import com.example.scanpro.domain.DeviceRepository
import com.example.scanpro.domain.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: DeviceRepository,
    private val mdns: MdnsDiscoveryManager
) : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices = _devices.asStateFlow()

    init {
        loadSavedDevices()
        startDiscovery()
    }

    private fun loadSavedDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDevices().collect { dbList ->
                _devices.value = dbList
            }
        }
    }

     fun startDiscovery() {
        viewModelScope.launch(Dispatchers.IO) {
            mdns.discoverDevices().collect { serviceInfo ->

                val ip = serviceInfo.host?.hostAddress ?: "Unknown"
                val name = serviceInfo.serviceName ?: "Unknown"
                if (ip == "Unknown" && name == "Unknown") {
                    Log.e("MDNS", "Device resolved but no IP — skipping")
                    return@collect
                }
                Log.d("sdfsdf",ip)
                Log.d("sdfsdf",name)

                val device = Device(
                    ipAddress = ip,
                    name = name,
                    isOnline = true,
                    lastSeen = System.currentTimeMillis()
                )

                repo.saveDevice(device)
            }
        }
    }
}
