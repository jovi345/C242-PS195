package com.app.travel.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.travel.data.repo.UserRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: UserRepository) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }
}