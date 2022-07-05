package com.okashitech.githubkotlinlist.presentation.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.okashitech.githubkotlinlist.R
import com.okashitech.githubkotlinlist.data.model.Repo
import com.okashitech.githubkotlinlist.databinding.ItemLayoutRepoBinding
import com.okashitech.githubkotlinlist.extensions.changeOrHideText
import com.okashitech.githubkotlinlist.extensions.inflateImageUrl
import com.okashitech.githubkotlinlist.utils.GlideApp
import com.okashitech.githubkotlinlist.utils.GlideRequest
import com.okashitech.githubkotlinlist.utils.GlideRequests

class RepoViewHolder(
    private val binding: ItemLayoutRepoBinding,
    private val glideApp: GlideRequests,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createViewHolder(parent: ViewGroup, glide: GlideRequests): RepoViewHolder =
            RepoViewHolder(
                ItemLayoutRepoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), glide
            )
    }

    fun bind(repo: Repo, listener: View.OnClickListener) {
        with(binding) {
            name.text = repo.fullName
            description.text = repo.description
            stars.text = repo.stars.toString()
            forks.text = repo.forks.toString()
            watchers.text = repo.watchers.toString()
            language.changeOrHideText(
                repo.mainTopics.joinToString(separator = ", "),
                View.INVISIBLE
            )
            avatar.inflateImageUrl(repo.owner.avatar_url, glideApp)
        }
        setOnClickListener(listener)
    }

    private fun setOnClickListener(listener: View.OnClickListener?) {
        binding.root.setOnClickListener(listener)
    }

    fun onRecycleItem() {
        setOnClickListener(null)
    }
}