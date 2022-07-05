package com.okashitech.githubkotlinlist.utils.networkstate

import android.content.Context
import androidx.lifecycle.LifecycleOwner

interface NetworkStateListener : LifecycleOwner {
    fun networkStateChanged(networkState: NetworkState)
    fun getContext(): Context
}