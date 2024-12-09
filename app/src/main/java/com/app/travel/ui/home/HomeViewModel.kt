package com.app.travel.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.travel.data.pref.UserModel
import com.app.travel.data.repo.UserRepository
import com.app.travel.data.response.DataItem
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _recommendationsByLocation = MutableLiveData<List<RecommendationResponse?>>()
    val recommendationsByLocation: LiveData<List<RecommendationResponse?>> = _recommendationsByLocation

    private val _recommendationsByHistory = MutableLiveData<List<RecommendationResponse?>>()
    val recommendationsByHistory: LiveData<List<RecommendationResponse?>> = _recommendationsByHistory

    fun fetchRecommendationsByLocation(location: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getRecommendationService().getRecommendations(location)
                _recommendationsByLocation.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRecommendationsByHistory(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getHistory(token)
                response.data?.let { data ->
                    _recommendationsByHistory.postValue(data.map { it?.toRecommendationResponse() })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun DataItem.toRecommendationResponse(): RecommendationResponse {
        return RecommendationResponse(
            id = this.id.toString(),
            placeName = this.placeName,
            city = this.city,
            category = this.category,
            imageUrl = this.imageUrl,
            description = this.description
        )
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}