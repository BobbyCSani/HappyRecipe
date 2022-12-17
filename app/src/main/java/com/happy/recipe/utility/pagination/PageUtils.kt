package com.happy.recipe.utility.pagination

import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource

object PageUtils {
    const val FIRST_PAGE = 1
    const val NETWORK_PAGE_SIZE = 20
    private const val PREFETCH_DISTANCE = 1

    fun getPrevPage(params: PagingSource.LoadParams<Int>): Int? {
        val currentPage = params.key ?: FIRST_PAGE
        return if (currentPage == FIRST_PAGE) null
        else currentPage.minus(1)
    }

    fun getNextPage(params: PagingSource.LoadParams<Int>, pageCount: Int): Int? {
        val currentPage = params.key ?: FIRST_PAGE
        return if (currentPage >= pageCount) null
        else currentPage.plus(1)
    }

    private fun createPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            initialLoadSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = PREFETCH_DISTANCE
        )
    }

    fun <T : Any> createPager(pagingSourceFactory: () -> PagingSource<Int, T>) = Pager(
        config = createPagingConfig(),
        pagingSourceFactory = pagingSourceFactory,
    )
}