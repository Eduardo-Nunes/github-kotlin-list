package com.okashitech.githubkotlinlist.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.MemoryCategory
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

private const val GLIDE_HTTP_LOAD_TIMEOUT = 30 * 10000
private const val IMAGE_REQUEST_TIME_OUT = 15L
private const val ONE_DAY_IN_MS = (24 * 60 * 1000)

@GlideModule
class GlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemorySizeCalculator(MemorySizeCalculator.Builder(context).build())
        builder.setDefaultRequestOptions(requestOptions())
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder()
            .readTimeout(IMAGE_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(IMAGE_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .build()

        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
        glide.setMemoryCategory(MemoryCategory.LOW)
        super.registerComponents(context, glide, registry)
    }

    private fun requestOptions(): RequestOptions {
        return RequestOptions()
            .signature(ObjectKey(System.currentTimeMillis() / ONE_DAY_IN_MS))
            .encodeFormat(Bitmap.CompressFormat.PNG)
            .encodeQuality(100)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .timeout(GLIDE_HTTP_LOAD_TIMEOUT)
            .format(DecodeFormat.PREFER_RGB_565)
            .skipMemoryCache(true)
    }

}