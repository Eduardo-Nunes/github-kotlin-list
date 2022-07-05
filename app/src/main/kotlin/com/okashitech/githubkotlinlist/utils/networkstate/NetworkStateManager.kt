package com.okashitech.githubkotlinlist.utils.networkstate

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.*

class NetworkStateManager(
    private val networkListener: NetworkStateListener
) {

    private val networkRequest: NetworkRequest by lazy {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    private var lastNetworkState: NetworkState? = null

    private fun registerNetWorkCallback(context: Context) {
        getConnectivityManager(context).registerNetworkCallback(networkRequest, networkCallback)
        getConnectivityManager(context).requestNetwork(networkRequest, networkCallback)
    }

    private fun unregisterNetWorkCallback(context: Context) {
        getConnectivityManager(context).unregisterNetworkCallback(networkCallback)
    }

    private fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (lastNetworkState != null) {
                networkListener.networkStateChanged(NetworkState.ONLINE)
            }
            lastNetworkState = NetworkState.ONLINE
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            networkListener.networkStateChanged(NetworkState.OFFLINE)
            lastNetworkState = NetworkState.OFFLINE
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

    }

    private fun start() {
        try {
            registerNetWorkCallback(networkListener.getContext())
        } catch (e: Exception) {
        }
    }

    private fun stop() {
        try {
            lastNetworkState = null
            unregisterNetWorkCallback(networkListener.getContext())
        } catch (e: Exception) {
        }
    }

    //  Lifecycle nextEvents
    private val lifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> start()
            Lifecycle.Event.ON_PAUSE -> stop()
            Lifecycle.Event.ON_DESTROY -> removeListeners()
            else -> Unit
        }
    }

    private fun addListeners() {
        networkListener.lifecycle.addObserver(lifecycleObserver)
    }

    private fun removeListeners() {
        networkListener.lifecycle.removeObserver(lifecycleObserver)
    }

    init {
        addListeners()
    }
}