package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class Ruangan(

	@field:SerializedName("data")
	val data: List<DataRuangan?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataRuangan(

	@field:SerializedName("jadwal")
	val jadwal: String? = null,

	@field:SerializedName("jumlah_peserta")
	val jumlahPeserta: String? = null,

	@field:SerializedName("nomor_peserta")
	val nomorPeserta: String? = null,

	@field:SerializedName("id_gedung")
	val idGedung: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama_ruangan")
	val namaRuangan: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("nama_gedung")
	val namaGedung: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
