package com.happy.recipe.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeDetailModel(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val image: String,
    @ColumnInfo val summary: String,
    @ColumnInfo val servings: Int,
    @ColumnInfo val readyInMinutes: Int,
    @ColumnInfo val extendedIngredients: ArrayList<Ingredient>,
    @ColumnInfo val instructions: String?,
    @ColumnInfo val spoonacularSourceUrl: String,
    @ColumnInfo val creditsText: String,
    @ColumnInfo val sourceUrl: String
)

data class Ingredient(val original: String)

fun RecipeDetailModel.toRecipeModel() =
    RecipeModel(
        id,
        title,
        image,
        summary,
        servings,
        readyInMinutes
    )