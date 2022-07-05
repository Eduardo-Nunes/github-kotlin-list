package com.okashitech.githubkotlinlist.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.okashitech.githubkotlinlist.api.ApiClient
import com.okashitech.githubkotlinlist.api.GithubService

private const val KOTLIN_REPOS = "language:kotlin"
private const val STARS_SORT = "stars"
private const val DEFAULT_PAGE_SIZE = 20
private const val MAX_PAGE_SIZE = 100

class GithubRepository(private val apiClient: ApiClient) {

    private val githubApi: GithubService by lazy {
        apiClient.createService(GithubService::class.java)
    }

    fun listKotlinReposByStar() =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                maxSize = MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GithubPagingDataSource(
                    githubApi,
                    STARS_SORT,
                    KOTLIN_REPOS
                )
            }
        ).flow
}