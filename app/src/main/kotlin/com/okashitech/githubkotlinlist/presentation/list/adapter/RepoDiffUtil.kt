package com.okashitech.githubkotlinlist.presentation.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.okashitech.githubkotlinlist.data.model.Repo

class RepoDiffUtil : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
        oldItem == newItem
}