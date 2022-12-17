package com.happy.recipe.feature.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happy.recipe.data.model.DiffUtilsRecipeModel
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.databinding.AdapterRecipeCardBinding

class RecipeCardAdapter(
    private val onRecipeClick: (Int) -> Unit
): PagingDataAdapter<RecipeModel, RecipeCardAdapter.ViewHolder>(DiffUtilsRecipeModel()) {

    inner class ViewHolder(private val binding: AdapterRecipeCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: RecipeModel) = with(binding){
            recipeCard.setData(data)
            recipeCard.setOnClickListener{
                onRecipeClick(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterRecipeCardBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { data ->
            holder.bind(data)
        }
    }

}