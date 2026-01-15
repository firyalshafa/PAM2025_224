package com.example.peyewaan.modeldata

import kotlinx.serialization.Serializable

/**
 * Response umum (insert, update, delete, cancel)
 */
@Serializable
data class ApiResponse(
    val status: String,
    val message: String
)

/**
 * Response register
 */
@Serializable
data class RegisterResponse(
    val status: String,
    val message: String
)

/**
 * Response khusus riwayat pesanan
 */
@Serializable
data class RiwayatResponse(
    val status: String,
    val message: String,
    val data: List<Order> = emptyList()
)

/**
 * Response detail satu pesanan (edit)
 */
@Serializable
data class DetailOrderResponse(
    val status: String,
    val message: String,
    val data: Order? = null
)
