package com.okashitech.githubkotlinlist.extensions

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

val Context.shortDelayAnimTime: Long
    get() = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

val Context.mediumAnimTime: Long
    get() = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

val Context.longAnimTime: Long
    get() = resources.getInteger(android.R.integer.config_longAnimTime).toLong()

val Context.windowWidth: Int
    get() {
        val metrics = DisplayMetrics()
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }