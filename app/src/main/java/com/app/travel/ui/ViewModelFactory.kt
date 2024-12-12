package com.app.travel.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.travel.data.repo.Injection
import com.app.travel.data.repo.UserRepository
import com.app.travel.ui.account.AccountViewModel
import com.app.travel.ui.auth.login.LoginViewModel
import com.app.travel.ui.auth.register.RegisterViewModel
import com.app.travel.ui.destination.DestinationViewModel
import com.app.travel.ui.detail.DetailViewModel
import com.app.travel.ui.explore.ExploreViewModel
import com.app.travel.ui.home.HomeViewModel
import com.app.travel.ui.search.SearchViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DestinationViewModel::class.java) -> {
                DestinationViewModel(repository) as T
            }
//            modelClass.isAssignableFrom(ExploreViewModel::class.java) -> {
//                ExploreViewModel(repository) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val repository = Injection.provideRepository(context)
                val instance = ViewModelFactory(repository)
                INSTANCE = instance
                instance
            }
        }
    }
}