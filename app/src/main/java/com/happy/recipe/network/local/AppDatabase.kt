package com.happy.recipe.network.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.happy.recipe.data.model.Converters
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.data.model.RecipeModel

@Database(entities = [RecipeModel::class, RecipeDetailModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recipeDetailDao(): RecipeDetailDao
}