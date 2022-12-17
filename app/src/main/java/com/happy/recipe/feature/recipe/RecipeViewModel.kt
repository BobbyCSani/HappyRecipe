package com.happy.recipe.feature.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.data.repository.RecipeRepository
import com.happy.recipe.utility.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipeRepository): ViewModel() {

    private val _recipeState = MutableLiveData<ViewState<PagingData<RecipeModel>>?>(null)
    val recipeState: LiveData<ViewState<PagingData<RecipeModel>>?>
        get() = _recipeState

    fun getFavorite() = viewModelScope.launch {
        setPagingData(repository.getFavoritePaging())
    }

    fun getRandomRecipe() = viewModelScope.launch {
        setPagingData(repository.getRandomRecipes())
    }

    fun searchRecipe(query: String) = viewModelScope.launch {
        setPagingData(repository.getRecipes(query))
    }

    private suspend fun setPagingData(request: Flow<PagingData<RecipeModel>>) {
        request.cachedIn(viewModelScope).catch { throwable ->
            setState(ViewState.Error(Exception(throwable)))
        }.collect { data ->
            setState(ViewState.Success(data))
        }
    }

    private fun setState(state: ViewState<PagingData<RecipeModel>>){
        _recipeState.value = state
    }
}