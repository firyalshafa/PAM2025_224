package com.example.peyewaan.modeldata

import kotlinx.serialization.Serializable
@Serializable
data class Order(
    val id_pesanan: Int,
    val id_user: Int,
    val id_alat: Int,
    val nama_alat: String? = null,
    val jumlah: Int,
    val total_harga: Double,
    val metode_pembayaran: String? = null, // Tambahkan ?
    val status_pesanan: String? = null,    // Tambahkan ?
    val tanggal_pesanan: String? = null,   // Tambahkan ?
    val tgl_pengambilan: String? = null,   // Tambahkan ?
    val tgl_pengembalian: String? = null    // Tambahkan ?
)