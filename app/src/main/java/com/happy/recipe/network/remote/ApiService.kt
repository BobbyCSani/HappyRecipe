package com.happy.recipe.network.remote

import com.happy.recipe.data.model.RandomRecipeResponseModel
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.data.model.RecipeResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("complexSearch")
    suspend fun getRecipes(
        @Query("offset") offset: Int = 0,
        @Query("number") size: Int = 20,
        @Query("query") query: String,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true
    ): Response<RecipeResponseModel>

    @GET("autocomplete")
    suspend fun getAutocompleteSearch(
        @Query("number") size: Int = 10,
        @Query("query") query: String
    ): Response<List<RecipeModel>>

    @GET("random")
    suspend fun getRandomRecipes(
        @Query("offset") offset: Int = 0,
        @Query("number") size: Int = 20
    ): Response<RandomRecipeResponseModel>

    @GET("{recipeId}/information")
    suspend fun getRecipeDetail(
        @Path("recipeId") recipeId: Int
    ): Response<RecipeDetailModel>
}