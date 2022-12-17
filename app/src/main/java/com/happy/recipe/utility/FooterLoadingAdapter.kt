package com.happy.recipe.utility

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happy.recipe.databinding.FooterLoadingBinding

class FooterLoadingAdapter : LoadStateAdapter<FooterLoadingAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = FooterLoadingBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(private val view: FooterLoadingBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(loadState: LoadState) {
            view.loading.isVisible(loadState is LoadState.Loading)
        }
    }
}