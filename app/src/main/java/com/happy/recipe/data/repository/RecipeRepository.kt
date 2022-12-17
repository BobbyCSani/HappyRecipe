package com.happy.recipe.data.repository

import androidx.paging.PagingData
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.data.model.RecipeModel
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipeDetail(recipeId: Int): Flow<RecipeDetailModel?>
    suspend fun getRecipes(query: String): Flow<PagingData<RecipeModel>>
    suspend fun getRandomRecipes(): Flow<PagingData<RecipeModel>>
    suspend fun getFavoritePaging(): Flow<PagingData<RecipeModel>>
    suspend fun getSearchSuggestion(query: String): Flow<List<String>>
    suspend fun getFavorite(): Flow<List<RecipeModel>>
    suspend fun insertFavorite(data: RecipeDetailModel)
    suspend fun deleteFavorite(data: RecipeDetailModel)
    suspend fun isFavorite(recipeId: Int): Flow<Boolean>
}