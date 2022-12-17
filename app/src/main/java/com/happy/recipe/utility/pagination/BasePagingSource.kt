package com.happy.recipe.utility.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState

class BasePagingSource<T : Any>(
    private val onResponse: suspend (LoadParams<Int>) -> PagingDataModel<T>
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val response = onResponse.invoke(params)
            val prevKey = PageUtils.getPrevPage(params)
            val nextKey = PageUtils.getNextPage(params, response.pageCount)
            if (response.isSuccess) {
                LoadResult.Page(
                    data = response.data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else LoadResult.Error((response.errorBody ?: Exception("Failed")) as Throwable)
        } catch (exception: Throwable) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}