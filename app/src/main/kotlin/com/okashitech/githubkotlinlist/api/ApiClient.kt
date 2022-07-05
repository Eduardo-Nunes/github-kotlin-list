package com.okashitech.githubkotlinlist.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient(
    private var baseUrl: String,
    private var okHttpClient: OkHttpClient
) {

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofitBuilder.client(okHttpClient).build().create(serviceClass)
    }
}