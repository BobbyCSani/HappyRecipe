package com.happy.recipe.feature.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happy.recipe.databinding.AdapterSearchSuggestionBinding

class SearchSuggestionAdapter(
    private val onSuggestionClick: (String) -> Unit,
    private val onCopySuggestionClick: (String) -> Unit
): RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder>() {

    private val list: MutableList<String> = mutableListOf()

    inner class ViewHolder(private val binding: AdapterSearchSuggestionBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: String) = with(binding){
            tvSuggestion.text = data
            tvSuggestion.setOnClickListener {
                onSuggestionClick(data)
            }
            btnSuggestion.setOnClickListener {
                onCopySuggestionClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterSearchSuggestionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submitData(listData: List<String>){
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

}