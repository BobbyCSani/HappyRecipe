package com.happy.recipe.data.repository

import androidx.paging.PagingData
import androidx.room.withTransaction
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.data.model.toRecipeModel
import com.happy.recipe.network.local.AppDatabase
import com.happy.recipe.network.remote.ApiService
import com.happy.recipe.utility.pagination.BasePagingSource
import com.happy.recipe.utility.pagination.PageUtils
import com.happy.recipe.utility.pagination.PagingDataModel
import com.happy.recipe.utility.toOffset
import com.happy.recipe.utility.toPageCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase): RecipeRepository {

    override suspend fun getRecipeDetail(recipeId: Int): Flow<RecipeDetailModel?> {
        return flow {
            val recipeDao = database.recipeDetailDao()
            val local = recipeDao.getDetailRecipe(recipeId)
            if (local.isNotEmpty()) {
                emit(local[0])
                return@flow
            }
            val response = apiService.getRecipeDetail(recipeId)
            if (response.isSuccessful){
                response.body()?.let { data ->
                    recipeDao.insertRecipe(listOf(data))
                }
            }
            emit(response.body())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getRecipes(query: String): Flow<PagingData<RecipeModel>> =
        PageUtils.createPager {
            BasePagingSource{ loadParam ->
                val response = apiService.getRecipes(
                    query = query,
                    offset = loadParam.key.toOffset()
                )
                PagingDataModel(
                    responseCode = 0,
                    data = response.body()?.results ?: emptyList(),
                    pageCount = response.body()?.totalResults.toPageCount(),
                    isSuccess = response.isSuccessful,
                    errorBody = response.errorBody()
                )
            }
        }.flow.flowOn(Dispatchers.IO)

    override suspend fun getRandomRecipes(): Flow<PagingData<RecipeModel>> =
        PageUtils.createPager {
        BasePagingSource{ loadParam ->
            val response = apiService.getRandomRecipes(
                offset = loadParam.key.toOffset()
            )
            PagingDataModel(
                responseCode = 0,
                data = response.body()?.recipes ?: emptyList(),
                pageCount = 1,
                isSuccess = response.isSuccessful,
                errorBody = response.errorBody()
            )
        }
    }.flow.flowOn(Dispatchers.IO)

    override suspend fun getFavoritePaging(): Flow<PagingData<RecipeModel>> =
        PageUtils.createPager {
            BasePagingSource{
                database.withTransaction {
                    val response = database.favoriteDao().getAllFavoriteRecipes()
                    PagingDataModel(
                        responseCode = 0,
                        data = response,
                        pageCount = 1,
                        isSuccess = true
                    )
                }
            }
        }.flow.flowOn(Dispatchers.IO)

    override suspend fun getSearchSuggestion(query: String): Flow<List<String>> =
        flow {
            val response = apiService.getAutocompleteSearch(query = query)
            response.body()?.map { recipeModel ->
                recipeModel.title
            }?.let { listString -> emit(listString) } ?: listOf<String>()
        }.flowOn(Dispatchers.IO)

    override suspend fun getFavorite(): Flow<List<RecipeModel>> =
        database.favoriteDao().getFavoriteRecipes().flowOn(Dispatchers.IO)

    override suspend fun insertFavorite(data: RecipeDetailModel) {
        withContext(Dispatchers.IO) {
            val recipeModel = data.toRecipeModel()
            database.favoriteDao().insertFavorite(listOf(recipeModel))
        }
    }

    override suspend fun deleteFavorite(data: RecipeDetailModel) {
        withContext(Dispatchers.IO) {
            val recipeModel = data.toRecipeModel()
            database.favoriteDao().deleteFavorite(recipeModel)
        }
    }

    override suspend fun isFavorite(recipeId: Int): Flow<Boolean> =
        flow {
            emit(database.favoriteDao().getFavorite(recipeId).isNotEmpty())
        }.flowOn(Dispatchers.IO)
}