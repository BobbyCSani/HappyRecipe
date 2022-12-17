package com.happy.recipe.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.happy.recipe.R
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.databinding.FavoriteRecipeCardBinding

class UIKitFavoriteRecipe(context: Context, attributeSet: AttributeSet?): ConstraintLayout(context, attributeSet) {

    private val binding = FavoriteRecipeCardBinding.inflate(LayoutInflater.from(context), this)

    fun setData(data: RecipeModel) = with(binding){
        Glide.with(context).load(data.image).into(thumbnail)
        title.text = data.title
        person.text = context.getString(R.string.person_count, data.servings)
        servingTime.text = context.getString(R.string.cooking_time, data.readyInMinutes)
        seeAll.visibility = GONE
    }

    fun setLoadMore() = with(binding){
        title.visibility = GONE
        person.visibility = GONE
        servingTime.visibility = GONE
        seeAll.visibility = VISIBLE

    }
}