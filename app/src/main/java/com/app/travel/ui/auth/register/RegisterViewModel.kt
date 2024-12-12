package com.app.travel.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.travel.data.pref.UserModel
import com.app.travel.data.repo.UserRepository
import com.app.travel.data.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: UserRepository) : ViewModel(){
    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() =_registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun register(username: String, email: String, password: String, confirmPassword: String, userLocation: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(username, email, password, confirmPassword, userLocation)
                _registerResult.postValue(response.message ?: "Registration successful")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()?.let { json ->
                    Gson().fromJson(json, ErrorResponse::class.java).message ?: "An error occurred"
                } ?: "An error occurred"
                _registerResult.postValue(errorMessage)
            } catch (e: Exception) {
                _registerResult.postValue(e.toString())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}