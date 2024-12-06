package com.app.travel.data.repo

import android.content.Context
import com.app.travel.data.pref.UserPreference
import com.app.travel.data.pref.dataStore
import com.app.travel.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService("")
        return UserRepository.getInstance(pref, apiService)
    }
}