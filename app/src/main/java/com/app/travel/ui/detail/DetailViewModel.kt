package com.app.travel.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.travel.data.pref.UserModel
import com.app.travel.data.repo.UserRepository
import com.app.travel.data.response.PlaceDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _placeDetail = MutableLiveData<PlaceDetail>()
    val placeDetail: LiveData<PlaceDetail> get() = _placeDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getPlaceDetail(id: String, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getPlaceDetail(id, token)
                _placeDetail.postValue(response.data)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getSession(): Flow<UserModel> {
        return repository.getSession()
    }

    fun logToken() {
        viewModelScope.launch {
            repository.getSession().collect { userModel ->
                Log.d("DetailViewModel", "Token: ${userModel.token}")
            }
        }
    }
}