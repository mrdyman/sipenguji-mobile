package com.diman.sipenguji.model

import com.google.gson.annotations.SerializedName

data class RuanganDetail(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Data(

	@field:SerializedName("nomor_peserta")
	val nomorPeserta: String? = null,

	@field:SerializedName("nisn")
	val nisn: String? = null,

	@field:SerializedName("jenis_ujian")
	val jenisUjian: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("jadwal")
	val jadwal: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama_ruangan")
	val namaRuangan: String? = null,

	@field:SerializedName("sesi")
	val sesi: String? = null,

	@field:SerializedName("nama_gedung")
	val namaGedung: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
