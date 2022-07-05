package com.okashitech.githubkotlinlist

import android.app.Application
import com.facebook.stetho.Stetho
import com.okashitech.githubkotlinlist.Modules.repositoryModule
import com.okashitech.githubkotlinlist.Modules.serviceModule
import com.okashitech.githubkotlinlist.Modules.viewModelsModule
import com.okashitech.githubkotlinlist.Modules.webViewModule
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin

class AppContext : Application() {

    private val backgroundCoroutineDispatcher by lazy { Dispatchers.IO }
    private val isDebug by lazy { BuildConfig.DEBUG }

    override fun onCreate() {
        super.onCreate()
        if (isDebug) {
            setupStetho()
        }
        setupKoinModules()
    }

    private fun setupStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun setupKoinModules() {
        startKoin {
            modules(
                listOf(
                    serviceModule(isDebug),
                    repositoryModule(),
                    viewModelsModule(backgroundCoroutineDispatcher),
                    webViewModule(backgroundCoroutineDispatcher)
                )
            )
        }
    }


}