package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class GambarRuangan(

	@field:SerializedName("data")
	val data: List<DataGambar?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataGambar(

	@field:SerializedName("id_ruangan")
	val idRuangan: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
