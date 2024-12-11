package com.app.travel.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.travel.data.response.CategoryResponseItem
import com.app.travel.data.response.SurveyRequest
import com.app.travel.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private val _surveyResults = MutableStateFlow<List<CategoryResponseItem>>(emptyList())
    val surveyResults: StateFlow<List<CategoryResponseItem>> = _surveyResults

    fun submitSurvey(request: SurveyRequest) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getRecommendationService()
                val response = apiService.getSurveyRecommendations(request)
                _surveyResults.value = response
            } catch (e: Exception) {
                // Handle the error appropriately
            }
        }
    }
}