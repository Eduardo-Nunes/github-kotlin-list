package com.okashitech.githubkotlinlist.base

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.okashitech.githubkotlinlist.components.infobar.InfoBarProvider
import com.okashitech.githubkotlinlist.components.infobar.InfoBarView
import com.okashitech.githubkotlinlist.extensions.hasConnectionAlert
import com.okashitech.githubkotlinlist.extensions.noConnectionAlert
import com.okashitech.githubkotlinlist.utils.networkstate.NetworkState
import com.okashitech.githubkotlinlist.utils.networkstate.NetworkStateListener
import com.okashitech.githubkotlinlist.utils.networkstate.NetworkStateManager

abstract class BaseActivity : AppCompatActivity(), InfoBarProvider, NetworkStateListener {

    protected open var infoBar: InfoBarView? = null
    private var networkStateManager: NetworkStateManager? = null

    protected fun setupInfoBar(viewGroup: ViewGroup, bar: InfoBarView? = null) {
        infoBar = bar ?: InfoBarView(viewGroup)
        networkStateManager = NetworkStateManager(this)
    }

    override fun getInfoBarInstance(): InfoBarView? {
        return infoBar
    }

    override fun networkStateChanged(networkState: NetworkState) {
        when (networkState) {
            NetworkState.OFFLINE -> {
                infoBar?.noConnectionAlert()
            }
            NetworkState.ONLINE -> {
                infoBar?.hasConnectionAlert()
            }
        }
    }

    override fun onDestroy() {
        infoBar = null
        networkStateManager = null
        super.onDestroy()
    }

    override fun getContext(): Context {
        return this
    }
}