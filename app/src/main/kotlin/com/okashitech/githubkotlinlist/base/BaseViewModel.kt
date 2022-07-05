package com.okashitech.githubkotlinlist.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    private val ioScope: CoroutineContext
) : ViewModel() {

    protected val backgroundScope by lazy {
        CoroutineScope(viewModelScope.coroutineContext + ioScope)
    }

    override fun onCleared() {
        backgroundScope.coroutineContext.cancelChildren()
        super.onCleared()
    }

}