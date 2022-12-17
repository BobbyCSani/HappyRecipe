package com.happy.recipe.feature.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: RecipeRepository): ViewModel() {

    private val _favoriteLiveData = MutableLiveData<List<RecipeModel>>(emptyList())
    val favoriteLiveData: LiveData<List<RecipeModel>>
        get() = _favoriteLiveData

    fun getFavorite() = viewModelScope.launch {
        repository.getFavorite().collect{ listRecipe ->
            _favoriteLiveData.value = listRecipe
        }
    }
}