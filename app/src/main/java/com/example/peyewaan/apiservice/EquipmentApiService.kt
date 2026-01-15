package com.example.peyewaan.apiservice

import com.example.peyewaan.modeldata.Equipment
import retrofit2.http.GET
import retrofit2.http.Query

interface EquipmentApiService {
    // UBAH get_alat.php MENJADI list_alat.php SESUAI FOLDER KAMU
    @GET("equipment/list_alat.php")
    suspend fun getAlat(): List<Equipment>

    // Jika kamu punya file detail, pastikan namanya juga benar
    @GET("equipment/get_alat_by_id.php")
    suspend fun getAlatById(@Query("id_alat") id: Int): Equipment
}