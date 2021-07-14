package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class Banner(

	@field:SerializedName("data")
	val data: List<DataBanner?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataBanner(

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
)
