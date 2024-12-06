package com.app.travel.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? =null
)
data class LoginResult(
    val email: String,
    val token: String
)