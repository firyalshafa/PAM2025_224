package com.example.peyewaan.uicontroller.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peyewaan.modeldata.Equipment
import com.example.peyewaan.repository.AppRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AppRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    var equipmentList by mutableStateOf<List<Equipment>>(emptyList())
        private set

    var filteredList by mutableStateOf<List<Equipment>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    init {
        refresh()
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        applyFilter()
    }

    private fun applyFilter() {
        val q = searchQuery.trim().lowercase()
        filteredList = if (q.isBlank()) {
            equipmentList
        } else {
            equipmentList.filter { alat ->
                alat.nama_alat.lowercase().contains(q)
            }
        }
    }

    // ✅ ini dipanggil dari HalamanHome (ON_RESUME)
    fun refresh() {
        viewModelScope.launch {
            isLoading = true
            isError = false
            try {
                // ✅ pastikan nama fungsi repo ini ada:
                // kalau repo kamu beda, ganti baris ini saja
                equipmentList = repository.getAlatOlahraga()

                applyFilter()
            } catch (e: Exception) {
                isError = true
                equipmentList = emptyList()
                filteredList = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}
