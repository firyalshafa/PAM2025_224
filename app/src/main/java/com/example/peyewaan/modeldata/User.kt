package com.example.peyewaan.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id_user: Int, // Harus Int sesuai int(11) di database
    val nama: String,
    val email: String
)