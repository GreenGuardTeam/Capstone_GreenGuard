package com.example.greenguard.data

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("hasil_prediksi")
	val hasilPrediksi: HasilPrediksi? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class HasilPrediksi(

	@field:SerializedName("solusi_penyakit")
	val solusiPenyakit: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("indeks_kelas")
	val indeksKelas: Int? = null,

	@field:SerializedName("label_penyakit")
	val labelPenyakit: String? = null,

	@field:SerializedName("gejala_penyakit")
	val gejalaPenyakit: String? = null,

	@field:SerializedName("dampak_penyakit")
	val dampakPenyakit: String? = null,

	@field:SerializedName("tips_dan_trick")
	val tipsDanTrick: String? = null
)
