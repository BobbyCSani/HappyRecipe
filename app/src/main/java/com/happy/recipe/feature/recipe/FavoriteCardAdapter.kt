package com.happy.recipe.feature.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.databinding.AdapterFavoriteBinding

class FavoriteCardAdapter(
    private val onRecipeClick: (Int) -> Unit,
    private val onLoadMoreClick: () -> Unit
): RecyclerView.Adapter<FavoriteCardAdapter.ViewHolder>() {

    private val list: MutableList<RecipeModel> = mutableListOf()

    inner class ViewHolder(private val binding: AdapterFavoriteBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: RecipeModel) = with(binding){
            favoriteCard.setData(data)
            favoriteCard.setOnClickListener {
                onRecipeClick(data.id)
            }
        }

        fun bindLoadMore() = with(binding){
            favoriteCard.setLoadMore()
            favoriteCard.setOnClickListener {
                onLoadMoreClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterFavoriteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < 5) holder.bind(list[position])
        else holder.bindLoadMore()
    }

    override fun getItemCount(): Int = if (list.size < 5) list.size else 6

    fun submitData(listData: List<RecipeModel>){
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

}