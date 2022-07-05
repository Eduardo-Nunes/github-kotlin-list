package com.okashitech.githubkotlinlist.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.okashitech.githubkotlinlist.api.GithubService
import com.okashitech.githubkotlinlist.data.model.Repo
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE = 1

class GithubPagingDataSource(
    private val githubApi: GithubService,
    private val sortedBy: String,
    private val query: String,
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: START_PAGE

        return try {
            val response = githubApi.searchRepos(query, sortedBy, position, params.loadSize)

           LoadResult.Page(
               data = response.items,
               prevKey = if (position == START_PAGE) null else position - 1,
               nextKey = if (response.items.isEmpty()) null else position + 1
           )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }
}