package com.happy.recipe.feature.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happy.recipe.R
import com.happy.recipe.databinding.ActivityHomeBinding
import com.happy.recipe.feature.recipe.*
import com.happy.recipe.feature.search.SearchActivity
import com.happy.recipe.utility.gone
import com.happy.recipe.utility.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var favoriteAdapter: FavoriteCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapter()
        setListener()
        setObserver()
        viewModel.getFavorite()
        setupRecipeFragment()
    }

    private fun setAdapter(){
        favoriteAdapter = FavoriteCardAdapter(::onFavoriteCardClick, ::onLoadMoreClick)
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, RecyclerView.HORIZONTAL, false)
            adapter = favoriteAdapter
        }
    }

    private fun setObserver(){
        viewModel.favoriteLiveData.observe(this@HomeActivity){ data ->
            if (data.isEmpty()) {
                binding.description.visible()
                binding.rvFavorite.gone()
            } else {
                binding.description.gone()
                binding.rvFavorite.visible()
                binding.favoriteHeading.setOnClickListener {
                    onLoadMoreClick()
                }
                favoriteAdapter.submitData(data)
            }
        }
    }

    private fun setListener() = with(binding){
        search.setOnClickListener {
            startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
        }

        favorite.setOnClickListener {
            onLoadMoreClick()
        }
    }

    private fun setupRecipeFragment(){
        val fragment = RecipeCardFragment.newInstance(RecipeDomainType.Home)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, RecipeCardFragment.TAG)
            .commit()
    }

    private fun onFavoriteCardClick(id: Int){
        RecipeDetailActivity.start(this, id)
    }

    private fun onLoadMoreClick(){
        if (binding.description.isVisible.not()) {
            ListRecipeActivity.start(this, RecipeDomainType.Favorite)
        }
    }


}