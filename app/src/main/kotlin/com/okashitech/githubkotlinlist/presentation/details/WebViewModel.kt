package com.okashitech.githubkotlinlist.presentation.details

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import com.okashitech.githubkotlinlist.base.BaseViewModel
import kotlin.coroutines.CoroutineContext

class WebViewModel(
    private val params: WebNavArgs,
    coroutineContext: CoroutineContext
) : BaseViewModel(coroutineContext) {

    var webUrl: MutableLiveData<Pair<String, WebViewClient>> = MutableLiveData()
    var failure: MutableLiveData<Boolean> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    private val webViewClient by lazy {
        object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                if (view.contentHeight < 1) {
                    view.clearView()
                    view.reload()
                } else {
                    loading.postValue(false)
                }
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                failure.postValue(true)
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                failure.postValue(true)
                super.onReceivedError(view, request, error)
            }
        }
    }

    fun retry() = loadUrl()

    private fun loadUrl() {
        if ( params.webUrl.isEmpty() || params.webUrl.isBlank()) {
            return failure.postValue(true)
        }
        loading.postValue(true)
        failure.postValue(false)
        webUrl.postValue(params.webUrl to webViewClient)
    }

    fun clearDataBinds() {
        webUrl = MutableLiveData()
        failure = MutableLiveData()
    }

    init {
        loadUrl()
    }

}