package com.happy.recipe.utility.pagination

import okhttp3.ResponseBody
import java.lang.Exception

data class PagingDataModel<T>(
    val responseCode: Int,
    val data: List<T>,
    val pageCount: Int,
    val isSuccess: Boolean = true,
    val errorBody: ResponseBody? = null
)