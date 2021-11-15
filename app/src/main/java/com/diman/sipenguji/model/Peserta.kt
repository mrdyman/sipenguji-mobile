package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class Peserta(

	@field:SerializedName("data")
	val data: List<DataPeserta?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataPeserta(

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("nomor_peserta")
	val nomorPeserta: String? = null,

	@field:SerializedName("nisn")
	val nisn: String? = null
)
