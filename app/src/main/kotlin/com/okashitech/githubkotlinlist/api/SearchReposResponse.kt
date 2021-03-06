package com.okashitech.githubkotlinlist.api

import com.google.gson.annotations.SerializedName
import com.okashitech.githubkotlinlist.data.model.Repo

data class SearchReposResponse(
    @SerializedName("total_count")
    val total: Int = 0,
    @SerializedName("items")
    val items: List<Repo> = emptyList(),
    val nextPage: Int? = null
)
