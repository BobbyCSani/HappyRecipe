package com.happy.recipe.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.data.repository.RecipeRepository
import com.happy.recipe.utility.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: RecipeRepository): ViewModel() {

    private val _suggestionState = MutableLiveData(listOf<String>())
    val suggestionState: LiveData<List<String>>
        get() = _suggestionState

    fun getSuggestion(query: String) = viewModelScope.launch {
        repository.getSearchSuggestion(query).collect{ result ->
            _suggestionState.value = result
        }
    }
}