package com.app.travel.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.travel.data.pref.UserModel
import com.app.travel.data.repo.UserRepository
import com.app.travel.data.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel( private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                response.loginResult?.token?.let { token ->
                    saveSession(UserModel(email, token, isLogin = true))
                }
                _loginResult.postValue(response.status?: "Login successful")
//                if (response.status == "Login successful") {
//                    response.loginResult?.let { user ->
//                        repository.saveSession(UserModel(user.email, user.token, true))
//                    }
//                }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()?.let { json ->
                    Gson().fromJson(json, ErrorResponse::class.java).message ?: "An error occurred"
                } ?: "An error occurred"
                _loginResult.postValue(errorMessage)
            } catch (e: Exception) {
                _loginResult.postValue(e.toString())
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
    fun getSession(): Flow<UserModel> {
        return repository.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}