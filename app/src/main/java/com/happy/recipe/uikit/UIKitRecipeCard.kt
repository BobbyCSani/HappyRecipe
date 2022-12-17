package com.happy.recipe.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.happy.recipe.R
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.databinding.RecipeCardBinding

class UIKitRecipeCard(context: Context, attributeSet: AttributeSet?): ConstraintLayout(context, attributeSet) {

    private val binding = RecipeCardBinding.inflate(LayoutInflater.from(context), this)

    fun setData(data: RecipeModel) = with(binding){
        Glide.with(context).load(data.image).into(thumbnail)
        title.text = data.title
        summary.text = HtmlCompat.fromHtml(data.summary, HtmlCompat.FROM_HTML_MODE_COMPACT)
        person.text = context.getString(R.string.person_count, data.servings)
        servingTime.text = context.getString(R.string.cooking_time, data.readyInMinutes)
    }
}