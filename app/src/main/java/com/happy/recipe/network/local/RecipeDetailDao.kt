package com.happy.recipe.network.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.happy.recipe.data.model.RecipeDetailModel

@Dao
interface RecipeDetailDao {

    @Query("SELECT * FROM recipedetailmodel WHERE id=(:id)")
    fun getDetailRecipe(id: Int): List<RecipeDetailModel>

    @Insert
    fun insertRecipe(data: List<RecipeDetailModel>)
}