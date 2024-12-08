package com.app.travel.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wishlist (
    @PrimaryKey(autoGenerate = false)
    val id: String = "",

    val placeName: String? = null,
    val imageUrl: String? = null
)