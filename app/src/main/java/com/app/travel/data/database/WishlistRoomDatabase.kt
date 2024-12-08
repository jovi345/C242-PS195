package com.app.travel.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Wishlist::class], version = 1, exportSchema = false)
abstract class WishlistRoomDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao

    companion object {
        @Volatile
        private var INSTANCE: WishlistRoomDatabase? = null

        fun getDatabase(context: Context): WishlistRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WishlistRoomDatabase::class.java,
                    "wishlist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}