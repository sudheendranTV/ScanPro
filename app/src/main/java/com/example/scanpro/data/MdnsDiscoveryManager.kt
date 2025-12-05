import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class MdnsDiscoveryManager(context: Context) {

    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    private val serviceTypes = listOf(
        "_airplay._tcp.",
        "_googlecast._tcp.",
        "_http._tcp."
    )

    private val activeListeners = mutableSetOf<NsdManager.DiscoveryListener>()

    fun discoverDevices() = callbackFlow<NsdServiceInfo> {

        serviceTypes.forEach { type ->
            val listener = createListener(this)

            try {
                nsdManager.discoverServices(
                    type,
                    NsdManager.PROTOCOL_DNS_SD,
                    listener
                )
                activeListeners.add(listener)

            } catch (e: Exception) {
                Log.e("MDNS", "Failed to start discovery for $type: $e")
            }
        }

        awaitClose {
            val copy = activeListeners.toList()
            copy.forEach { safeStop(it) }
            activeListeners.clear()
        }
    }

    private fun createListener(flow: SendChannel<NsdServiceInfo>) =
        object : NsdManager.DiscoveryListener {

            override fun onDiscoveryStarted(serviceType: String) {
                Log.d("MDNS", "Started: $serviceType")
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                Log.d("MDNS", "Found: ${serviceInfo.serviceName} — Resolving...")

                nsdManager.resolveService(serviceInfo,
                    object : NsdManager.ResolveListener {
                        override fun onServiceResolved(resolved: NsdServiceInfo) {
                            Log.d("MDNS", "Resolved: ${resolved.serviceName}")
                            flow.trySend(resolved)
                        }

                        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                            Log.e("MDNS", "Resolve failed: $errorCode")
                        }
                    })
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                Log.d("MDNS", "Lost: ${serviceInfo.serviceName}")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.d("MDNS", "Stopped: $serviceType")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("MDNS", "Start failed: $errorCode")
                safeStop(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("MDNS", "Stop failed: $errorCode")
                safeStop(this)
            }
        }

    private fun safeStop(listener: NsdManager.DiscoveryListener) {
        if (!activeListeners.contains(listener)) return

        try {
            nsdManager.stopServiceDiscovery(listener)
        } catch (e: IllegalArgumentException) {
            Log.w("MDNS", "Listener was not registered")
        } catch (e: Exception) {
            Log.e("MDNS", "Stop failed: $e")
        } finally {
            activeListeners.remove(listener)
        }
    }
}
