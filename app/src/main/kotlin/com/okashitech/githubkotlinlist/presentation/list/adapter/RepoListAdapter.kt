package com.okashitech.githubkotlinlist.presentation.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.okashitech.githubkotlinlist.R
import com.okashitech.githubkotlinlist.data.model.Repo
import com.okashitech.githubkotlinlist.utils.GlideApp
import com.okashitech.githubkotlinlist.utils.GlideRequests

class RepoListAdapter(
    private val glideRequests: GlideRequests,
    private val onItemClickCallback: (Repo) -> Unit
) : PagingDataAdapter<Repo, RepoViewHolder>(RepoDiffUtil()) {

    private val onViewClickListener by lazy {
        View.OnClickListener {
            val position = it.getTag(R.string.position) as? Int ?: return@OnClickListener
            getItem(position)?.run(onItemClickCallback)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder =
        RepoViewHolder.createViewHolder(parent, glideRequests)

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let { repo ->
            holder.bind(repo, onViewClickListener)
            holder.itemView.setTag(R.string.position, position)
        }
    }

    override fun onViewRecycled(holder: RepoViewHolder) {
        holder.itemView.setTag(R.string.position, null)
        holder.onRecycleItem()
        super.onViewRecycled(holder)
    }
}