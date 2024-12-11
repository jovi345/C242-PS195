package com.app.travel.data.retrofit

import com.app.travel.data.response.CategoryResponse
import com.app.travel.data.response.CategoryResponseItem
import com.app.travel.data.response.LoginResponse
import com.app.travel.data.response.PlaceDetailResponse
import com.app.travel.data.response.RecomendLastSeenResponse
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.data.response.RegisterResponse
import com.app.travel.data.response.SurveyRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("destination/recommendation-cb/{location}")
    suspend fun getRecommendations(
        @Path("location") location: String
    ) : List<RecommendationResponse>

    @GET("destination/place/{id}")
    suspend fun getPlaceDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ) : PlaceDetailResponse

    @GET("destination/recommend/history")
    suspend fun getHistory(
        @Header("Authorization") token: String
    ) : RecomendLastSeenResponse

    @GET("destination/search")
    suspend fun searchDestinations(
        @Query("q") query: String
    ): CategoryResponse

    @GET("destination/category/{category}")
    suspend fun getDestinationsByCategory(
        @Path("category") category: String
    ): CategoryResponse

    @POST("destination/survey-recommendation")
    suspend fun getSurveyRecommendations(
        @Body request: SurveyRequest
    ) : List<CategoryResponseItem>
}

