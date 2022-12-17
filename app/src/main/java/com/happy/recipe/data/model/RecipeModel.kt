package com.happy.recipe.data.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class RecipeResponseModel(
    val totalResults: Int,
    val results: List<RecipeModel>
)

data class RandomRecipeResponseModel(
    val recipes: List<RecipeModel>
)

@Entity
data class RecipeModel(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val image: String,
    @ColumnInfo val summary: String,
    @ColumnInfo val servings: Int,
    @ColumnInfo val readyInMinutes: Int,
)

class DiffUtilsRecipeModel : DiffUtil.ItemCallback<RecipeModel>() {
    override fun areItemsTheSame(
        oldItem: RecipeModel,
        newItem: RecipeModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: RecipeModel,
        newItem: RecipeModel
    ): Boolean {
        return oldItem == newItem
    }
}
