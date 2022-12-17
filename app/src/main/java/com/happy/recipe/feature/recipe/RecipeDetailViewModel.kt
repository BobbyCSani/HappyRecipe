package com.happy.recipe.feature.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(private val repository: RecipeRepository): ViewModel() {

    private val _recipeDetailLiveData = MutableLiveData<RecipeDetailModel>()
    val recipeDetailLiveData: LiveData<RecipeDetailModel>
        get() = _recipeDetailLiveData

    fun getRecipeDetail(recipeId: Int) = viewModelScope.launch {
        repository.getRecipeDetail(recipeId).collect{ data ->
            _recipeDetailLiveData.value = data
        }
    }

    fun favorite(data: RecipeDetailModel) = viewModelScope.launch {
        repository.insertFavorite(data)
    }

    fun deleteFavorite(data: RecipeDetailModel) = viewModelScope.launch {
        repository.deleteFavorite(data)
    }

    suspend fun isFavorite(recipeId: Int) = repository.isFavorite(recipeId)
}