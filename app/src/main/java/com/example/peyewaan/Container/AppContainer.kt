package com.example.peyewaan.Container



import com.example.peyewaan.apiservice.ApiClient
import com.example.peyewaan.apiservice.AuthApiService
import com.example.peyewaan.apiservice.EquipmentApiService
import com.example.peyewaan.apiservice.OrderApiService
import com.example.peyewaan.repository.AppRepository
import com.example.peyewaan.repository.NetworkAppRepository

interface AppContainer {
    val appRepository: AppRepository
}

class DefaultAppContainer : AppContainer {
    // Inisialisasi Service dari ApiClient
    private val authService: AuthApiService by lazy { ApiClient.createService(AuthApiService::class.java) }
    private val equipmentService: EquipmentApiService by lazy { ApiClient.createService(EquipmentApiService::class.java) }
    private val orderService: OrderApiService by lazy { ApiClient.createService(OrderApiService::class.java) }

    // Masukkan service ke dalam Repository
    // Di dalam file AppContainer.kt bagian DefaultAppContainer
    override val appRepository: AppRepository by lazy {
        NetworkAppRepository(authService, equipmentService, orderService)
    }
}