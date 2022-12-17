package com.happy.recipe.network.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.happy.recipe.data.model.RecipeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM recipemodel LIMIT 5")
    fun getFavoriteRecipes(): Flow<List<RecipeModel>>

    @Query("SELECT * FROM recipemodel WHERE id=(:id)")
    fun getFavorite(id: Int): List<RecipeModel>

    @Query("SELECT * FROM recipemodel")
    fun getAllFavoriteRecipes(): List<RecipeModel>

    @Insert
    fun insertFavorite(data: List<RecipeModel>)

    @Delete
    fun deleteFavorite(data: RecipeModel)
}