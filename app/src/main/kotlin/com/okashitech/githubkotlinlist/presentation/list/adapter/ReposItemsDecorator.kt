package com.okashitech.githubkotlinlist.presentation.list.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.okashitech.githubkotlinlist.R

class ReposItemsDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val startEndPadPX = view.resources.getDimensionPixelSize(R.dimen.margin_horizontal)
        val betweenPadPX = view.resources.getDimensionPixelSize(R.dimen.items_distance)

        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.set(outRect.left, startEndPadPX, outRect.right, betweenPadPX)
            state.itemCount - 1 -> outRect.set(outRect.left, outRect.top, outRect.right, startEndPadPX)
            else -> outRect.set(outRect.left, outRect.top, outRect.right, betweenPadPX)
        }
    }
}