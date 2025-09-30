package com.demo.cleanproject.utils.commonUtils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkStateListener {
    fun onNetworkAvailable()
    fun onNetworkLost()
    fun onNetworkStateChanged(isConnected: Boolean)
}

@Singleton
class NetworkHelper @Inject constructor(@param:ApplicationContext private val context: Context) {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var listeners = mutableSetOf<NetworkStateListener>()

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        return checkNetworkConnectionModern()
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun checkNetworkConnectionModern(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                ) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getConnectionType(): String {
        return getConnectionTypeModern()
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun getConnectionTypeModern(): String {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "NONE"

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
            else -> "UNKNOWN"
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun startNetworkMonitoring() {
        if (networkCallback != null) return // Already monitoring

        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
    }

    fun stopNetworkMonitoring() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            networkCallback = null
        }
    }

    private fun createNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val hasInternet =
                    capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

                if (hasInternet) {
                    notifyNetworkAvailable()
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                notifyNetworkLost()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val hasInternet =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                if (hasInternet) {
                    notifyNetworkAvailable()
                } else {
                    notifyNetworkLost()
                }
            }
        }
    }

    private fun notifyNetworkAvailable() {
        listeners.forEach { it.onNetworkAvailable() }
        listeners.forEach { it.onNetworkStateChanged(true) }
    }

    private fun notifyNetworkLost() {
        listeners.forEach { it.onNetworkLost() }
        listeners.forEach { it.onNetworkStateChanged(false) }
    }

    fun addNetworkListener(listener: NetworkStateListener) {
        listeners.add(listener)
    }

    fun removeNetworkListener(listener: NetworkStateListener) {
        listeners.remove(listener)
    }

    fun clearNetworkListeners() {
        listeners.clear()
    }
}