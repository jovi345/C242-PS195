package com.app.travel.data.pref

data class UserModel(
    val email: String,
    val username: String,
    val userLocation: String,
    val token: String,
    val isLogin: Boolean = false
)