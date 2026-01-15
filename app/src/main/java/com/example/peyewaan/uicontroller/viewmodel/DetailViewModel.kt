package com.example.peyewaan.uicontroller.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peyewaan.modeldata.Equipment
import com.example.peyewaan.repository.AppRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel(private val repository: AppRepository) : ViewModel() {

    var detailUiState by mutableStateOf<Equipment?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // State untuk input user
    var jumlahSewa by mutableIntStateOf(1)
    var tglAmbil by mutableStateOf("")
    var tglKembali by mutableStateOf("")

    fun getEquipmentById(idAlat: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                detailUiState = repository.getEquipmentById(idAlat)
            } catch (e: Exception) {
                errorMessage = "HTTP 404 Not Found: Cek file get_alat_by_id.php"
            } finally {
                isLoading = false
            }
        }
    }

    fun hitungDurasi(): Long {
        if (tglAmbil.isEmpty() || tglKembali.isEmpty()) return 1
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val d1 = sdf.parse(tglAmbil)
            val d2 = sdf.parse(tglKembali)
            val diff = (d2?.time ?: 0) - (d1?.time ?: 0)
            val days = diff / (24 * 60 * 60 * 1000)
            if (days <= 0) 1 else days
        } catch (e: Exception) { 1 }
    }

    // LOGIKA KELIPATAN: Harga x Jumlah x Hari
    fun hitungTotalBayar(): Double {
        val hargaPerHari = detailUiState?.harga ?: 0.0
        return hargaPerHari * jumlahSewa * hitungDurasi()
    }

    fun buatPesanan(idUser: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.insertOrder(
                    idUser = idUser,
                    idAlat = detailUiState?.id_alat ?: 0,
                    jumlah = jumlahSewa,
                    tglAmbil = tglAmbil,
                    tglKembali = tglKembali,
                    totalHarga = hitungTotalBayar()
                )
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Gagal Memesan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}