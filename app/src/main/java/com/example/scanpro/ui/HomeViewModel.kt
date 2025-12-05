package com.example.scanpro.ui

import MdnsDiscoveryManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanpro.domain.DeviceRepository
import com.example.scanpro.domain.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: DeviceRepository,
    private val mdns: MdnsDiscoveryManager
) : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices = _devices.asStateFlow()
    private var scanJob: Job? = null

    init {
        loadSavedDevices()
        startDiscovery()
    }

    private fun loadSavedDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDevices().collect { dbList ->
                val updatedList = dbList.map { device ->
                    val isOnline = repo.isDeviceReachable(device.ipAddress, device.port, 1500)
                    val updatedDevice = device.copy(isOnline = isOnline)

                    repo.updateDevice(updatedDevice)

                    updatedDevice
                }
                _devices.value = updatedList
            }
        }
    }

    fun startDiscovery() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch(Dispatchers.IO) {
            mdns.discoverDevices().collect { serviceInfo ->

                val ip = serviceInfo.host?.hostAddress ?: "Unknown"
                val name = serviceInfo.serviceName ?: "Unknown"
                val port = serviceInfo.port
                if (ip == "Unknown" && name == "Unknown") {
                    Log.e("MDNS", "Device resolved but no IP — skipping")
                    return@collect
                }

                val device = Device(
                    ipAddress = ip,
                    name = name,
                    port = port,
                    isOnline = true,
                    lastSeen = System.currentTimeMillis()
                )

                repo.saveDevice(device)
            }
        }
    }
}
