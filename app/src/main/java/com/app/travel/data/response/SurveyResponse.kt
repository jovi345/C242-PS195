package com.app.travel.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SurveyResponse(

	@field:SerializedName("SurveyResponse")
	val surveyResponse: List<SurveyResponseItem?>? = null
)

@Parcelize
data class SurveyResponseItem(

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
	val rating: Double? = null,

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
) : Parcelable
