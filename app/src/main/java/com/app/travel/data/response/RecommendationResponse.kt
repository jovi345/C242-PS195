package com.app.travel.data.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
	@SerializedName("cluster")
	val cluster: Int? = null,

	@SerializedName("place_name")
	val placeName: String? = null,

	@SerializedName("lng")
	val lng: String? = null,

	@SerializedName("city")
	val city: String? = null,

	@SerializedName("image_url")
	val imageUrl: String? = null,

	@SerializedName("rating")
	val rating: Any? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("city_tag")
	val cityTag: String? = null,

	@SerializedName("phone")
	val phone: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("state")
	val state: String? = null,

	@SerializedName("category")
	val category: String? = null,

	@SerializedName("lat")
	val lat: String? = null,

	@SerializedName("reviews_count")
	val reviewsCount: Int? = null
)