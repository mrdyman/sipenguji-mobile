package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("data")
	val data: DataUser? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataUser(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("signature")
	val signature: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
