package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class GedungDetail(

	@field:SerializedName("data")
	val data: DetailGedung? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DetailGedung(

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("jumlah_ruangan")
	val jumlahRuangan: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("nama_gedung")
	val namaGedung: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
