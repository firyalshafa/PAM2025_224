package com.example.peyewaan.repository

import com.example.peyewaan.modeldata.Equipment
import com.example.peyewaan.modeldata.Order
import com.example.peyewaan.modeldata.User

interface AppRepository {

    // AUTH
    suspend fun loginUser(email: String, kataSandi: String): User?
    suspend fun registerUser(nama: String, email: String, kataSandi: String)

    // EQUIPMENT
    suspend fun getAlatOlahraga(): List<Equipment>
    suspend fun getEquipmentById(id: Int): Equipment

    // ORDER
    suspend fun getOrderHistory(idUser: Int): List<Order>
    suspend fun getOrderById(idOrder: Int): Order

    suspend fun insertOrder(
        idUser: Int,
        idAlat: Int,
        jumlah: Int,
        tglAmbil: String,
        tglKembali: String,
        totalHarga: Double
    )

    suspend fun updateOrder(idOrder: Int, jumlah: Int)

    // ✅ FIX: deleteOrder butuh idUser untuk cancel_order.php
    suspend fun deleteOrder(idOrder: Int, idUser: Int)

}
