package com.app.travel.data.repo

import androidx.datastore.preferences.core.edit
import com.app.travel.data.pref.UserModel
import com.app.travel.data.pref.UserPreference
import com.app.travel.data.response.CategoryResponse
import com.app.travel.data.response.CategoryResponseItem
import com.app.travel.data.response.LoginResponse
import com.app.travel.data.response.PlaceDetailResponse
import com.app.travel.data.response.RecomendLastSeenResponse
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.data.response.RegionResponse
import com.app.travel.data.response.RegisterResponse
import com.app.travel.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
){
    suspend fun register(username: String, email: String, password: String, confirmPassword: String, userLocation: String) : RegisterResponse {
        return apiService.register(username, email, password, confirmPassword, userLocation)
    }

    suspend fun login(email: String, password: String) : LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getPlaceDetail(id: String, token: String): PlaceDetailResponse {
        return withContext(Dispatchers.IO) {
            apiService.getPlaceDetail(id, "Bearer $token")
        }
    }
    suspend fun searchDestinations(query: String): List<CategoryResponseItem?> {
        return withContext(Dispatchers.IO) {
            val response = apiService.searchDestinations(query)
            response.data ?: emptyList()
        }
    }

    suspend fun getRegion(city: String): List<RegionResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getRegion(city)
        }
    }

    suspend fun getHistory(token: String): RecomendLastSeenResponse {
        return withContext(Dispatchers.IO) {
            apiService.getHistory("Bearer $token")
        }
    }

    suspend fun getDestinationsByCategory(category: String): List<CategoryResponseItem?> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getDestinationsByCategory(category)
            response.data ?: emptyList()
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getThemeSetting(): Flow<Boolean> {
        return userPreference.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        return userPreference.saveThemeSetting(isDarkModeActive)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}