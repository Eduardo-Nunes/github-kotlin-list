package com.okashitech.githubkotlinlist.extensions

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.ViewTreeObserver
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.okashitech.githubkotlinlist.utils.GlideApp
import com.okashitech.githubkotlinlist.utils.GlideRequest
import com.okashitech.githubkotlinlist.utils.GlideRequests

fun ImageView.changeOrHideImage(
    @DrawableRes drawable: Int?,
    delayHide: Long = context.shortDelayAnimTime
) {
    if (delayHide > 0) postDelayed(changeOrHideImage(this, drawable), delayHide)
    else changeOrHideImage(this, drawable).run()
}

private fun changeOrHideImage(imageView: ImageView, @DrawableRes drawable: Int?): Runnable =
    Runnable {
        imageView.visibility = View.INVISIBLE
        imageView.visibility = if (drawable == null) {
            View.GONE
        } else {
            imageView.setImageResource(drawable)
            View.VISIBLE
        }
    }

inline fun ImageView.addOnPreDrawListener(crossinline callback: () -> Unit) {
    this.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            callback.invoke()
            viewTreeObserver.removeOnPreDrawListener(this)
            return true
        }
    })
}

private const val PREVIEW_MULTIPLIER = 0.25f
private const val BIG_SIZE = 0.8
private const val NORMAL_MAX_SIZE = 0.5
private const val NORMAL_MIN_SIZE = 0.2

fun ImageView.inflateImageUrl(
    url: String?,
    glide: GlideRequests,
    withPreview: Boolean = true,
    withTransition: Boolean = true,
    bypassDifferType: Boolean = false,
    @DrawableRes withPlaceHolder: Int = android.R.drawable.picture_frame
) {
    try {
        if (url.isNullOrEmpty() || url.isNullOrBlank()) {
            changeOrHideImage(withPlaceHolder)
        } else {
            addOnPreDrawListener {
                glide.load(url)
                    .placeholder(withPlaceHolder)
                    .differType(measuredHeight >= measuredWidth, bypassDifferType)
                    .override(measuredWidth, measuredHeight)
                    .priority(getPriority())
                    .preview(withPreview)
                    .transition(withTransition)
                    .error(android.R.drawable.stat_notify_error)
                    .into(this)
            }
        }
    } catch (e: OutOfMemoryError) {
        GlideApp.with(this).setPauseAllRequestsOnTrimMemoryModerate(true)
        GlideApp.get(context).clearMemory()
    }
}

private fun <TranscodeType> GlideRequest<TranscodeType>.differType(
    isPortrait: Boolean,
    bypassDifferType: Boolean
): GlideRequest<TranscodeType> {
    if (bypassDifferType) return this
    return if (isPortrait) {
        this.fitCenter()
    } else {
        this.centerCrop()
    }
}

fun ImageView.getPriority(): Priority {
    val sizePercentage = measuredWidth.toFloat().div(context.windowWidth.toFloat())

    return when {
        sizePercentage >= BIG_SIZE -> Priority.IMMEDIATE
        sizePercentage >= NORMAL_MAX_SIZE -> Priority.HIGH
        NORMAL_MAX_SIZE > sizePercentage && sizePercentage > NORMAL_MIN_SIZE -> Priority.NORMAL
        else -> Priority.LOW
    }
}

private fun GlideRequest<Drawable>.transition(withTransition: Boolean): GlideRequest<Drawable> {
    return if (withTransition) {
        this.transition(
            DrawableTransitionOptions.with(
                DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            )
        )
    } else {
        this
    }
}

private fun GlideRequest<Drawable>.preview(hasPreview: Boolean): GlideRequest<Drawable> {
    return if (hasPreview) {
        this.thumbnail(PREVIEW_MULTIPLIER)
    } else {
        this
    }
}