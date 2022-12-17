package com.happy.recipe.feature.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happy.recipe.data.model.Ingredient
import com.happy.recipe.databinding.AdapterIngredientBinding

class IngredientAdapter: RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    private val list: MutableList<Ingredient> = mutableListOf()

    inner class ViewHolder(private val binding: AdapterIngredientBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: Ingredient) = with(binding){
            ingredient.text = data.original
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterIngredientBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submitData(listData: List<Ingredient>){
        list.clear()
        list.addAll(listData)
        notifyItemRangeChanged(0, list.size)
    }

}