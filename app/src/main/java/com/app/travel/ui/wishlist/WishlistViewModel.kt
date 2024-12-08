package com.app.travel.ui.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.travel.data.database.Wishlist
import com.app.travel.data.database.WishlistDao
import com.app.travel.data.database.WishlistRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishlistViewModel(application: Application) : AndroidViewModel(application) {
    private val wishlistDao: WishlistDao = WishlistRoomDatabase.getDatabase(application).wishlistDao()

    val allWishlist: LiveData<List<Wishlist>> = wishlistDao.getAllWishlist()

    fun addToWishlist(wishlist: Wishlist) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistDao.insert(wishlist)
        }
    }

    fun removeFromWishlist(wishlist: Wishlist) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistDao.delete(wishlist)
        }
    }
}