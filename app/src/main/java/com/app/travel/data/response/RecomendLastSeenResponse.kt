package com.app.travel.data.response

import com.google.gson.annotations.SerializedName

data class RecomendLastSeenResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("cluster")
	val cluster: Int? = null,

	@field:SerializedName("place_name")
	val placeName: String? = null,

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("city_tag")
	val cityTag: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("reviews_count")
	val reviewsCount: Int? = null
)
