package com.app.travel.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers

class SearchViewModel : ViewModel() {
    private val apiService = ApiConfig.getRecommendationService()

    fun searchDestinations(query: String) = liveData(Dispatchers.IO) {
        try {
            val response = apiService.searchDestinations(query)
            emit(response)
        } catch (e: Exception) {
            emit(emptyList<RecommendationResponse>())
        }
    }
}