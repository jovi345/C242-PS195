package com.app.travel.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wishlist: Wishlist)

    @Delete
    fun delete(wishlist: Wishlist)

    @Query("SELECT * FROM Wishlist WHERE id = :id")
    fun getWishlistById(id: String): LiveData<Wishlist?>

    @Query("SELECT * FROM Wishlist")
    fun getAllWishlist(): LiveData<List<Wishlist>>
}