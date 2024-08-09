package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api

import android.content.Context
import android.net.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class ConnectionManager @Inject constructor(@ApplicationContext private val context: Context) {
    val isConnected: Boolean
        get() = _isConnected.get()

    private val _isConnected = AtomicBoolean(false)

    init{
        listenToNetworkChanges()
    }

    private fun listenToNetworkChanges(){
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                _isConnected.set(true)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                _isConnected.set(false)
            }
        }

        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}