package com.example.peyewaan.uicontroller.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peyewaan.modeldata.Order
import com.example.peyewaan.repository.AppRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: AppRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var orderList by mutableStateOf<List<Order>>(emptyList())
        private set

    var jumlahEdit by mutableStateOf("")
        private set

    fun updateJumlahState(value: String) {
        jumlahEdit = value.filter { it.isDigit() }
    }

    fun getRiwayat(idUser: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                orderList = repository.getOrderHistory(idUser)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Gagal mengambil riwayat"
                orderList = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun getDetailOrder(idOrder: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val order = repository.getOrderById(idOrder)
                jumlahEdit = order.jumlah.toString()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Gagal mengambil detail pesanan"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateOrder(idOrder: Int, onSuccessBack: () -> Unit) {
        val jumlahBaru = jumlahEdit.toIntOrNull()
        if (jumlahBaru == null || jumlahBaru <= 0) {
            errorMessage = "Jumlah harus > 0"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.updateOrder(idOrder, jumlahBaru)
                onSuccessBack()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Gagal update"
            } finally {
                isLoading = false
            }
        }
    }

    // ✅ FIX: deleteOrder(idOrder, idUser) + refresh
    fun cancelOrder(idOrder: Int, idUser: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.deleteOrder(idOrder, idUser)
                getRiwayat(idUser)
            } finally { isLoading = false }
        }
    }



}
