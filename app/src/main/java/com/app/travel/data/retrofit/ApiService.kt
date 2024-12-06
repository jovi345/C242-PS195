package com.app.travel.data.retrofit

import com.app.travel.data.response.LoginResponse
import com.app.travel.data.response.PlaceDetailResponse
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register (
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String,
        @Field("userLocation") userLocation: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login (
        @Field("email") name: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("destination/recommendation-cb/{lokasi}")
    suspend fun getRecommendations(@Path("lokasi") lokasi: String): List<RecommendationResponse>

    @GET("destination/place/{id}")
    suspend fun getPlaceDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): PlaceDetailResponse
}

