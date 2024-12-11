package com.app.travel.data.response

import com.google.gson.annotations.SerializedName

data class RegionResponse(

	@field:SerializedName("data")
	val data: List<ItemData?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ItemData(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tag")
	val tag: String? = null,

	@field:SerializedName("region")
	val region: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)
