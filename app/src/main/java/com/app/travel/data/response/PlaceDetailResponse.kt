package com.app.travel.data.response

data class PlaceDetailResponse(
    val status: String,
    val data: PlaceDetail
)

data class PlaceDetail(
    val id: Int,
    val place_name: String,
    val state: String,
    val city: String,
    val city_tag: String,
    val phone: String,
    val category: String,
    val description: String,
    val image_url: String,
    val reviews_count: Int,
    val rating: Double,
    val lat: String,
    val lng: String,
    val cluster: Int
)
