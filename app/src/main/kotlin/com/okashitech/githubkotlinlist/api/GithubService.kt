package com.okashitech.githubkotlinlist.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    companion object {
        const val BASE_URL = "https://api.github.com/"
        const val SEARCH_ENDPOINT = "search/repositories"
        const val SORT_PARAM = "sort"
        const val QUERY_PARAM = "q"
        const val PAGE_PARAM = "page"
        const val SIZE_PARAM = "per_page"
    }

    @GET(SEARCH_ENDPOINT)
    suspend fun searchRepos(
        @Query(QUERY_PARAM) query: String,
        @Query(SORT_PARAM) by: String,
        @Query(PAGE_PARAM) page: Int,
        @Query(SIZE_PARAM) itemsPerPage: Int
    ): SearchReposResponse
}