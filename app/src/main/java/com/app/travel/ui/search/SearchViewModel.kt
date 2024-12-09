package com.app.travel.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.travel.data.repo.UserRepository
import com.app.travel.data.response.CategoryResponseItem
import com.app.travel.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: UserRepository) : ViewModel() {
    private val apiService = ApiConfig.getRecommendationService()

    private val _recommendationsByCategory = MutableLiveData<List<CategoryResponseItem?>>()
    val recommendationsByCategory: LiveData<List<CategoryResponseItem?>> = _recommendationsByCategory

    private val _searchResults = MutableLiveData<List<CategoryResponseItem?>>()
    val searchResults: LiveData<List<CategoryResponseItem?>> get() = _searchResults

    fun searchDestinations(query: String) {
        viewModelScope.launch {
            try {
                val results = repository.searchDestinations(query)
                _searchResults.postValue(results)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getDestinationsByCategory(category: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDestinationsByCategory(category)
                _recommendationsByCategory.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}