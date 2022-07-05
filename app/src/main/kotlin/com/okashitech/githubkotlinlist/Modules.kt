package com.okashitech.githubkotlinlist

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.okashitech.githubkotlinlist.api.ApiClient
import com.okashitech.githubkotlinlist.api.GithubService
import com.okashitech.githubkotlinlist.data.GithubRepository
import com.okashitech.githubkotlinlist.presentation.details.WebNavArgs
import com.okashitech.githubkotlinlist.presentation.details.WebViewModel
import com.okashitech.githubkotlinlist.presentation.list.GithubReposViewModel
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 150L

object Modules {

    private fun createOkHttpClient(
        interceptors: List<Interceptor?> = emptyList(),
        networkInterceptors: List<Interceptor?> = emptyList()
    ): OkHttpClient = with(OkHttpClient.Builder()) {
        interceptors.filterNotNull().forEach { addInterceptor(it) }
        networkInterceptors.filterNotNull().forEach { addNetworkInterceptor(it) }
        connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        build()
    }

    fun serviceModule(isDebug: Boolean) = module {
        single {
            ApiClient(
                baseUrl = GithubService.BASE_URL,
                okHttpClient = createOkHttpClient(
                    interceptors = listOf(
                        if (isDebug) {
                            val httpLoggingInterceptor = HttpLoggingInterceptor()
                            httpLoggingInterceptor.apply {
                                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                            }
                        } else null
                    ),
                    networkInterceptors = listOf(
                        if (isDebug) StethoInterceptor() else null
                    )
                )
            )
        }
    }

    fun repositoryModule() = module {
        single {
            GithubRepository(get())
        }
    }

    fun viewModelsModule(backgroundCoroutineDispatcher: CoroutineDispatcher) = module {
        single {
            GithubReposViewModel(backgroundCoroutineDispatcher, get())
        }
    }

    fun webViewModule(backgroundCoroutineDispatcher: CoroutineDispatcher) = module {
        viewModel { (navParams: WebNavArgs) ->
            WebViewModel(
                navParams,
                backgroundCoroutineDispatcher
            )
        }
    }
}