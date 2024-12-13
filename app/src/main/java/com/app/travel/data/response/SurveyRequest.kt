package com.app.travel.data.response

import com.google.gson.annotations.SerializedName

data class SurveyRequest(
    @SerializedName("mbti") val mbti: String,
    @SerializedName("location") val location: String,
    @SerializedName("preffered_category") val preferredCategory: String,
    @SerializedName("travel_style") val travelStyle: String,
    @SerializedName("age") val age: String,
    @SerializedName("travel_frequency") val travelFrequency: String
)

