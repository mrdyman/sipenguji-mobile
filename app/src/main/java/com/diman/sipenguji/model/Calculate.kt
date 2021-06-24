package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class Calculate(

	@field:SerializedName("titik_tujuan")
	val titikTujuan: String? = null,

	@field:SerializedName("titik_awal")
	val titikAwal: String? = null,

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
