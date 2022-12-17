package com.happy.recipe.feature.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.happy.recipe.databinding.ActivitySearchBinding
import com.happy.recipe.feature.recipe.ListRecipeActivity
import com.happy.recipe.feature.recipe.RecipeDomainType
import com.happy.recipe.utility.inputListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchSuggestionAdapter: SearchSuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapter()
        setListener()
        setObserver()
    }

    private fun setAdapter(){
        searchSuggestionAdapter = SearchSuggestionAdapter(::onSuggestionClick, ::onCopySuggestionClick)
        binding.rvSearchSuggestion.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchSuggestionAdapter
        }
    }

    private fun setListener() {
        binding.searchInput.inputListener(lifecycleScope,
            onKeywordEvent = { query ->
                viewModel.getSuggestion(query)
            }, searchAction = {
                binding.btnSearch.performClick()
            })
        binding.btnSearch.setOnClickListener {
            val queryEditable = binding.searchInput.text
            if (queryEditable.isNullOrEmpty().not()) {
                ListRecipeActivity.start(this, RecipeDomainType.Search(queryEditable.toString()))
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setObserver() {
        viewModel.suggestionState.observe(this) { data ->
            searchSuggestionAdapter.submitData(data)
        }
    }

    private fun onSuggestionClick(query: String){
        binding.searchInput.setText(query)
        binding.btnSearch.performClick()
    }

    private fun onCopySuggestionClick(query: String){
        binding.searchInput.setText(query)
    }
}