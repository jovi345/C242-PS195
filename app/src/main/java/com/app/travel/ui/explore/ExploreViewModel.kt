package com.app.travel.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.travel.data.response.CategoryResponseItem
import com.app.travel.data.response.SurveyRequest
import com.app.travel.data.response.SurveyResponse
import com.app.travel.data.response.SurveyResponseItem
import com.app.travel.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private val _surveyResults = MutableStateFlow<List<SurveyResponseItem>>(emptyList())
    val surveyResults: StateFlow<List<SurveyResponseItem>> = _surveyResults

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun submitSurvey(request: SurveyRequest) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val apiService = ApiConfig.getRecommendationService()
                val response = apiService.getSurveyRecommendations(request)
                _surveyResults.value = response // Respons langsung berupa List<SurveyResponseItem>
            } catch (e: Exception) {
                _errorMessage.value = "Failed to submit survey: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
