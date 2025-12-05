package com.example.scanpro.data

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.content.Context
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class MdnsDiscoveryManager(context: Context) {

    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    private val serviceTypes = listOf(
        "_airplay._tcp.",
        /*"_googlecast._tcp.",
        "_http._tcp.",
        "_raop._tcp.",
        "_dlna._tcp.",
        "_mediaserver._tcp." */
    )

    fun discoverDevices() = callbackFlow<NsdServiceInfo> {

        val listeners = mutableListOf<NsdManager.DiscoveryListener>()

        serviceTypes.forEach { type ->

            val listener = object : NsdManager.DiscoveryListener {

                override fun onDiscoveryStarted(serviceType: String) {
                    Log.d("MDNS", "Searching for $serviceType")
                }

                override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                    Log.d(
                        "MDNS",
                        "FOUND ➜ ${serviceInfo.serviceName} | Type: ${serviceInfo.serviceType}"
                    )
                    trySend(serviceInfo)
                }

                override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                    Log.d("MDNS", "Lost: ${serviceInfo.serviceName}")
                }

                override fun onDiscoveryStopped(serviceType: String) {
                    Log.d("MDNS", "Stopped: $serviceType")
                }

                override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                    Log.d("MDNS", "Start failed $serviceType: $errorCode")
                    nsdManager.stopServiceDiscovery(this)
                }

                override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                    Log.d("MDNS", "Stop failed $serviceType: $errorCode")
                    nsdManager.stopServiceDiscovery(this)
                }
            }

            listeners.add(listener)
            nsdManager.discoverServices(type, NsdManager.PROTOCOL_DNS_SD, listener)
        }

        awaitClose {
            listeners.forEach { listener ->
                nsdManager.stopServiceDiscovery(listener)
            }
        }
    }
}
