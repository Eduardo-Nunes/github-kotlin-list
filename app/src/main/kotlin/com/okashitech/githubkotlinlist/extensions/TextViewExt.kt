package com.okashitech.githubkotlinlist.extensions

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.GONE

fun TextView.changeOrHideText(
    value: String?,
    visible: Int = GONE
) {
    visibility = View.INVISIBLE
    visibility = if (value.isNullOrBlank()) {
        visible
    } else {
        text = value
        View.VISIBLE
    }
}

fun TextView.postChangeOrHideText(
    text: String?,
    delayHide: Long = context.shortDelayAnimTime
) {
    if (delayHide > 0) postOnAnimationDelayed(changeOrHideText(this, text), delayHide)
    else changeOrHideText(this, text).run()
}

private fun changeOrHideText(imageView: TextView, text: String?): Runnable = Runnable {
    imageView.visibility = View.INVISIBLE
    imageView.visibility = if (text.isNullOrBlank()) {
        GONE
    } else {
        imageView.text = text
        View.VISIBLE
    }
}