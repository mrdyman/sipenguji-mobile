package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class Gedung(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataItem(

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("jumlah_ruangan")
	val jumlahRuangan: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama_gedung")
	val namaGedung: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
