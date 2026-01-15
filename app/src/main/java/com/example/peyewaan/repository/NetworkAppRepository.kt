package com.example.peyewaan.repository

import com.example.peyewaan.apiservice.AuthApiService
import com.example.peyewaan.apiservice.EquipmentApiService
import com.example.peyewaan.apiservice.OrderApiService
import com.example.peyewaan.modeldata.Equipment
import com.example.peyewaan.modeldata.Order
import com.example.peyewaan.modeldata.User

class NetworkAppRepository(
    private val authApiService: AuthApiService,
    private val equipmentApiService: EquipmentApiService,
    private val orderApiService: OrderApiService
) : AppRepository {

    // ================= AUTH =================
    override suspend fun loginUser(email: String, kataSandi: String): User? =
        authApiService.login(email, kataSandi)

    override suspend fun registerUser(nama: String, email: String, kataSandi: String) {
        authApiService.register(nama, email, kataSandi)
    }

    // ================= EQUIPMENT =================
    override suspend fun getAlatOlahraga(): List<Equipment> =
        equipmentApiService.getAlat()

    override suspend fun getEquipmentById(id: Int): Equipment =
        equipmentApiService.getAlatById(id)

    // ================= ORDER =================
    override suspend fun getOrderHistory(idUser: Int): List<Order> {
        val res = orderApiService.getRiwayat(idUser)
        if (res.status != "success") throw Exception(res.message)
        return res.data
    }

    override suspend fun getOrderById(idOrder: Int): Order {
        val res = orderApiService.getDetailOrder(idOrder)
        if (res.status != "success") throw Exception(res.message)
        return res.data ?: throw Exception("Detail pesanan kosong")
    }

    override suspend fun insertOrder(
        idUser: Int,
        idAlat: Int,
        jumlah: Int,
        tglAmbil: String,
        tglKembali: String,
        totalHarga: Double
    ) {
        val res = orderApiService.insertOrder(idUser, idAlat, jumlah, tglAmbil, tglKembali, totalHarga)
        if (res.status != "success") throw Exception(res.message)
    }

    override suspend fun updateOrder(idOrder: Int, jumlah: Int) {
        val res = orderApiService.updateOrder(idOrder, jumlah)
        if (res.status != "success") throw Exception(res.message)
    }

    // ✅ FIX: kirim idUser bener
    override suspend fun deleteOrder(idOrder: Int, idUser: Int) {
        val res = orderApiService.cancelOrder(idOrder, idUser)
        if (res.status != "success") throw Exception(res.message)
    }


}
