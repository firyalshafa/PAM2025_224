package com.example.peyewaan.apiservice

import com.example.peyewaan.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiClient {
    // Konfigurasi JSON agar lebih fleksibel dan tidak mudah crash
    private val json = Json {
        ignoreUnknownKeys = true // Abaikan kolom SQL yang tidak ada di model Kotlin
        coerceInputValues = true // Paksa nilai default jika data dari PHP null
        isLenient = true         // Izinkan format JSON yang kurang rapi
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            // Pastikan IP di Constants.kt adalah 192.168.0.101 sesuai CMD kamu
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // Fungsi untuk membuat service (Auth, Equipment, Order)
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}