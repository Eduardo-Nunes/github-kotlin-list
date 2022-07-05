package com.okashitech.githubkotlinlist.components.infobar

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.forEach
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.okashitech.githubkotlinlist.R
import com.okashitech.githubkotlinlist.extensions.inflate
import com.okashitech.githubkotlinlist.extensions.changeOrHideImage
import com.okashitech.githubkotlinlist.extensions.postChangeOrHideText
import com.okashitech.githubkotlinlist.extensions.changeVisibility
import com.okashitech.githubkotlinlist.extensions.removeFromParent
import kotlinx.coroutines.Job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class InfoBarView(private val parent: ViewGroup) {

    private var hideJob: Job? = null
    private var foodBarsPositions = mutableListOf<Int>()

    private fun createInfoBarView(
        title: String,
        icon: Int? = null,
        description: String? = null
    ): View {
        return parent.inflate(R.layout.info_bar_layout).apply {
            findViewById<TextView>(R.id.infoBarTitle).text = title
            findViewById<ImageView>(R.id.infoBarIcon).changeOrHideImage(icon)
            findViewById<TextView>(R.id.infoBarDescription).postChangeOrHideText(description)
        }
    }

    fun show(
        @DrawableRes icon: Int? = null,
        @StringRes title: Int,
        @StringRes description: Int? = null,
        descriptionText: String? = null
    ) {
        hide()
        val infoView = createInfoBarView(
            icon = icon,
            title = parent.context.getString(title),
            description = description?.let { parent.context.getString(it) } ?: descriptionText,
        )

        val containerView = FrameLayout(parent.context).also {
            it.post {
                TransitionManager.beginDelayedTransition(it, Slide(Gravity.TOP))
                it.addView(
                    infoView,
                    ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                )
            }
        }

        parent.post {
            parent.addView(
                containerView,
                parent.childCount,
                ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            )
            foodBarsPositions.add(parent.childCount - 1)
        }

        infoView.setOnClickListener {
            hide()
        }
    }

    fun hideOnDelay(mDelay: Long) {
        hideJob = CoroutineScope(Dispatchers.Default).launch {
            delay(mDelay)
            hide()
        }
    }

    fun hide() {
        foodBarsPositions.sortedDescending().forEach { pos ->
            (parent.getChildAt(pos) as? FrameLayout)?.also { container ->
                CoroutineScope(Dispatchers.Main).launch {
                    container.forEach {
                        TransitionManager.beginDelayedTransition(
                            container,
                            Slide(Gravity.TOP)
                        )
                        it.changeVisibility(false)
                    }
                    container.removeFromParent()
                }
            }
            foodBarsPositions.clear()
        }
        hideJob?.cancel()
        hideJob = null
    }
}