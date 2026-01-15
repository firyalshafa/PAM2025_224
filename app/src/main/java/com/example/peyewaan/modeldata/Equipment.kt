package com.example.peyewaan.modeldata



import kotlinx.serialization.Serializable

@Serializable
data class Equipment(
    val id_alat: Int,
    val nama_alat: String,
    val deskripsi: String,
    val harga: Double,
    val stok: Int,
    val gambar_url: String? = null // Gunakan ? agar tidak crash jika link gambar kosong di SQL
)