package com.happy.recipe.feature.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.happy.recipe.R
import com.happy.recipe.databinding.FragmentRecipeBinding
import com.happy.recipe.utility.FooterLoadingAdapter
import com.happy.recipe.utility.ViewState
import com.happy.recipe.utility.isVisible
import com.happy.recipe.utility.parcelable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeCardFragment: Fragment() {

    companion object {
        const val TAG = "RecipeFragment"
        const val DOMAIN_TYPE = "DOMAIN_TYPE"

        fun newInstance(
            domainType: RecipeDomainType
        ) =
            RecipeCardFragment().apply {
                arguments = bundleOf(DOMAIN_TYPE to domainType)
            }
    }

    private val domainType by lazy { arguments?.parcelable<RecipeDomainType>(DOMAIN_TYPE) ?: RecipeDomainType.Home }
    private val recipeViewModel by viewModels<RecipeViewModel>()
    private var binding: FragmentRecipeBinding? = null
    private lateinit var recipeAdapter: RecipeCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecipeObserver()
        setupListener()
        getData()
    }

    private fun getData(){
        when(domainType){
            RecipeDomainType.Favorite -> recipeViewModel.getFavorite()
            RecipeDomainType.Home -> {
                binding?.swipeRefresh?.isEnabled = false
                recipeViewModel.getRandomRecipe()
            }
            is RecipeDomainType.Search -> {
                val query = (domainType as RecipeDomainType.Search).query
                recipeViewModel.searchRecipe(query)
                binding?.emptyDescription?.text = getString(R.string.query_recipe_not_found, query)
            }
        }
    }

    private fun setupListener(){
        binding?.swipeRefresh?.setOnRefreshListener {
            getData()
        }
    }

    private fun setupAdapter() {
        activity?.let { act ->
            recipeAdapter = RecipeCardAdapter { recipeId ->
                RecipeDetailActivity.start(act, recipeId)
            }
            recipeAdapter.addLoadStateListener { loadState ->
                if (loadState.refresh == LoadState.Loading) binding?.swipeRefresh?.isRefreshing = true
                else {
                    binding?.emptyDescription?.isVisible(recipeAdapter.itemCount <= 0)
                    binding?.swipeRefresh?.isRefreshing = false
                }
            }
            binding?.rvRecipe?.apply {
                layoutManager = LinearLayoutManager(act)
                adapter = recipeAdapter.withLoadStateFooter(FooterLoadingAdapter())
            }
        }
    }

    private fun setupRecipeObserver() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Error -> activity?.let { act ->
                    Toast.makeText(
                        act,
                        state.exception.message ?: act.getString(R.string.something_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is ViewState.Success -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            recipeAdapter.submitData(state.data)

                        }
                    }
                }
                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}