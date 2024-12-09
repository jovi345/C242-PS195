package com.app.travel.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? =null
)
data class LoginResult(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("user_location")
    val userLocation: String,

    @field:SerializedName("token")
    val token: String
)