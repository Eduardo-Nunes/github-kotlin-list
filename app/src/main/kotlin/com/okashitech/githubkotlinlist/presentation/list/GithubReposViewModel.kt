package com.okashitech.githubkotlinlist.presentation.list

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.okashitech.githubkotlinlist.base.BaseViewModel
import com.okashitech.githubkotlinlist.data.GithubRepository
import com.okashitech.githubkotlinlist.data.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class GithubReposViewModel(
    ioScope: CoroutineContext,
    private val repository: GithubRepository
) : BaseViewModel(ioScope) {

    private val _flowKotlinData =
        repository
            .listKotlinReposByStar()
            .cachedIn(backgroundScope)

    fun getData(): Flow<PagingData<Repo>> {
        return _flowKotlinData
    }
}